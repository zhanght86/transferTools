package service;

import bean.Node;
import bean.Pack;
import client.dao.NodeSelfDao;
import client.dao.PackDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 缓存池
 * 1 缓存自身信息
 * 2 缓存本节点发布的文件
 * 3 缓存从父节点请求的文件
 *
 * Created by QPing on 2015/4/6.
 */
public class CachePool {

    protected final Logger log = Logger.getLogger(CachePool.class);

    public static Node self;

    // TODO 预加载父节点
    public static Node parent;

    public static int fileReleaseMaxNo;        // 本地文件已发布的最大序号
    public static int fileUpdateMaxNo;        // 本地文件最大的序号（包括已发布和正在更新的）

    private static Vector<Pack> fileReleaseList;     // 文件发布列表，最后一个no最大
    private static Vector<Pack> fileUpdateList;      // 文件正在更新列表，最后一个no最大

    private CachePool(){
        log.info("从数据库中加载信息到缓存...");
        loadFileList();
        loadSelf();
    }

    public static boolean isClient() {
        if(null == self
                || self.getParentport() == 0
                || StringUtils.isBlank(self.getParentip())
                || StringUtils.isBlank(self.getInvitecode())
                || StringUtils.isBlank(self.getArea())
                || StringUtils.isBlank(self.getDeploytype())
                || StringUtils.isBlank(self.getNodetype())
                || StringUtils.isBlank(self.getName())
                || StringUtils.isBlank(self.getNodeid())
                || "center".equals(self.getNodetype())  // 中心节点，已经处于最顶层，不需要更新增量包了
                || "deploy".equals(self.getNodetype())  // 部署节点，更新由上一层节点通知后再进行
                || "127.0.0.1".equals(self.getParentip())
                || "localhost".equals(self.getParentip())
                ){
            return false;
        }
        return true;
    }

    /**
     * 将从父节点中请求过来的数据，保存在内存中，返回新增的列表在随后插入到本地数据库中
     * @param list
     * @return
     */
    public List<Pack> addDownloadList(List<Pack> list) {
        if(null == list || list.isEmpty()){
            return null;
        }

        synchronized (fileUpdateList){
            List<Pack> filterList = new ArrayList<Pack>();
            for(Pack p : list){
                if(p.getNo() > fileUpdateMaxNo){
                    p.setStatus(Pack.STATUS_UPDATING);
                    insertByOrder(fileUpdateList, p);
                    filterList.add(p);
                }
            }
            resetFileUpdateMaxNo();
            return filterList;
        }
    }

    public int getLastUpdateFileNo() {
        return fileUpdateMaxNo;
    }

    /**
     * 根据区域和子节点的序号过滤出文件列表
     * @param area
     * @param clientFileNo
     * @return
     */
    public List<Pack> filterRelease(String area, int clientFileNo) {
        // 子节点的最大序号已经是最新
        if(clientFileNo == fileReleaseMaxNo){
            return null;
        }
        List<Pack> list = new ArrayList<Pack>();
        // 将所有序列号大于fileNo的文件，过滤出来
        for(Pack pack : fileReleaseList){
            if(pack.getNo() > clientFileNo && pack.getArea().equals(area)){
                list.add(pack);
            }
        }
        return list;
    }

    public int checkUpdate(Pack pack) {
        int fileno = pack.getNo();

        if(fileno > fileUpdateMaxNo){
            // 未加入下载列表
            return 3;
        }

        if(fileno > fileUpdateMaxNo){
            // 正在下载
            return 2;
        }else{
            // 下载完成
            return 1;
        }
    }

    public static class CachePoolHolder{
        public static CachePool instance = new CachePool();
    }
    public static CachePool getInstance(){
        return CachePoolHolder.instance;
    }

    private void loadSelf() {
        log.info("加载自身信息...");
        // 加载自身节点信息
        NodeSelfDao selfDao = new NodeSelfDao();
        try {
            self = selfDao.query();
            // 如果之前没有启动过
            if(self == null){
                self = new Node();
                self.current();
                selfDao.insert(self);
            }
            // 如果软件是从另外一台机器上拷贝的
            if(beMoved()){
                selfDao.deleteAll();
                self = new Node();
                self.current();
                selfDao.insert(self);
            }

        } catch (SQLException e) {
            log.error(e);
        } finally {
            selfDao.close();
        }
    }

    private void loadFileList() {
        log.info("加载文件列表...");
        // 从数据库中读取增量包信息,只加载发布的
        PackDao packDao = new PackDao();
        try {
            fileReleaseList = packDao.queryAll("status = " + Pack.STATUS_RELEASED, "no asc");
            fileUpdateList = packDao.queryAll("status = " + Pack.STATUS_UPDATING, "no asc");
            resetFileUpdateMaxNo();
        } catch (SQLException e) {
            log.error(e);
        }finally {
            packDao.close();
        }
    }


    // 判断是否软件是否从一台计算机移动到另一台
    private boolean beMoved(){
        String macDB = self.getMac();
        String macCurrent = new Node().current().getMac();

        // mac地址发生变动，作废原来的注册信息，插入新的
        if(macDB == null || !macDB.equals(macCurrent)){
            return true;
        }

        return false;
    }

    /**
     * 获取当前系统中发布及更新文件中最大的序列号
     */
    public void resetFileUpdateMaxNo(){

        fileUpdateMaxNo = -1;
        fileReleaseMaxNo = -1;

        if(!fileReleaseList.isEmpty()){

            int lastIndexOfRelease = fileReleaseList.size() - 1;
            int temp = fileReleaseList.get(lastIndexOfRelease).getNo();
            fileReleaseMaxNo = temp;

            if(temp > fileUpdateMaxNo){
                fileUpdateMaxNo = temp;
            }
        }

        if(!fileUpdateList.isEmpty()){
            int lastIndexOfUpdate = fileUpdateList.size() - 1;
            int temp = fileUpdateList.get(lastIndexOfUpdate).getNo();
            if(temp > fileUpdateMaxNo){
                fileUpdateMaxNo = temp;
            }
        }

    }

    public void deleteRelease(int no) {

        synchronized (fileReleaseList){
            if(fileReleaseList == null) return;

            for(int i = 0 ; i < fileReleaseList.size(); i++){
                if(fileReleaseList.get(i).getNo() == no){
                    fileReleaseList.remove(i);
                    break;
                }
            }
            resetFileUpdateMaxNo();
        }
    }

    public void addRelease(Pack pack) {

        synchronized (fileReleaseList){
            if(fileReleaseList == null) return;
            insertByOrder(fileReleaseList, pack);
            resetFileUpdateMaxNo();
        }
    }

    public Vector<Pack> getReleaseList(){
        return fileReleaseList;
    }

    public int getReleaseMaxNo(){
        return fileReleaseMaxNo;
    }


    private void insertByOrder(Vector<Pack> list,Pack pack){
        int lastIndex = list.size() - 1;

        int no = pack.getNo();
        // 命中注定的位置
        int positionDecreedByFate = 0;

        // 从最后一个位置开始找，找到这个pack命中注定的位置
        for(int i = lastIndex; i > -1; i--){
            int curNo = list.get(i).getNo();
            if(no > curNo){
                positionDecreedByFate = i + 1;
                break;
            }
        }
        list.add(positionDecreedByFate, pack);
    }

    public static boolean needDownload() {
        return !(null == CachePool.fileUpdateList || CachePool.fileUpdateList.size() == 0);
    }



    public Pack getUpdate() {
        return fileUpdateList.get(0);
    }


    public void removeUpdate(Pack p) {
        CachePool.fileUpdateList.remove(p);
    }


    public Vector<Pack> getUpdateList() {
        return CachePool.fileUpdateList;
    }


}
