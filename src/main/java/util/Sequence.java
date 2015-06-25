package util;

import client.dao.BaseDao;

import java.sql.Connection;
import java.sql.ResultSet;

public class Sequence {
    private static Connection connection;
    private static Sequence sequence;
    private static final String DEFAULT_SEQ_NAME = "seq";
    private static final String TABLE_SEQUENCE = "sys_sequence";

    private Sequence(){}

    private static class SequenceHolder{
        public static Sequence instance = new Sequence();
    }
    public static Sequence getInstance(){
        return SequenceHolder.instance;
    }

    public int getSeqNo() throws Exception {
        return getSeqNo(DEFAULT_SEQ_NAME);
    }

    public synchronized int getSeqNo(String seqName) throws Exception {
        if(null == seqName || "".equals(seqName)){
            seqName = DEFAULT_SEQ_NAME;
        }
        String querySQL = "select value from " + TABLE_SEQUENCE + " where seqname = '" + seqName +"'";
        BaseDao baseDao = new BaseDao();
        int seqno = -1;
        try{
            ResultSet rs = baseDao.executeQuery(querySQL, null);

            if(rs.next()){
                seqno = rs.getInt("value");
                updateSequence(baseDao, seqName, seqno + 1);
            }else{
                // 生成新的序列
                createSequence(baseDao, seqName);
                seqno = 0;
            }
        }catch(Exception ex){

        }finally {
            baseDao.close();
        }
        return seqno;
    }

    private void updateSequence(BaseDao baseDao, String seqName, int nextValue) throws Exception {
        String updateSQL = "update " + TABLE_SEQUENCE + " set value = " + nextValue + " where seqname = '" + seqName +"'";
        int row = baseDao.executeUpdate(updateSQL, null);
        if(row != 1){
            throw new Exception("更新序列失败!");
        }
    }

    private void createSequence(BaseDao baseDao, String seqName) throws Exception {
        String insertSQL = "insert into " + TABLE_SEQUENCE + " values('" + seqName + "', 1)";
        int row = baseDao.executeUpdate(insertSQL, null);
        if(row != 1){
            throw new Exception("生成新序列失败，无法插入记录!");
        }
    }

}
