-------system---db---init-----------
---- version : 0.8              ----
---- auto-init : true           ----
-----前3行除版本号的数字其他不可修改----

-- 系统版本
DROP TABLE IF EXISTS sys_version;
create table sys_version(
    version varchar(100),
    createtime varchar(30)		--创建时间
);

-- 序列表
DROP TABLE IF EXISTS sys_sequence;
create table sys_sequence(
    seqname varchar(30),
    value int
);
insert into sys_sequence values('seq', 0);     --默认序列

-- 系统配置表
DROP TABLE IF EXISTS sys_setting;
create table sys_setting(
    id int,
    type varchar(30),
    name varchar(30),
    value varchar(30)
);

insert into sys_setting(id, name, value) values(3, 'privatekey', '');     -- 私钥
insert into sys_setting(id, name, value) values(4, 'publickey', '');      -- 公钥

-- 中心节点下属子节点信息表
DROP TABLE IF EXISTS node_child;
create table node_child(
    nodeid varchar(100),
    name varchar(100),
    parentid varchar(100),
    nodetype varchar(30),
    area varchar(100),
    ip varchar(100),
    port int,
    mac varchar(100),
    javaversion varchar(30),
    osname varchar(100),
    osversion varchar(100),
    hostname varchar(100),
    registertime varchar(30),
    lastconnecttime varchar(30),
    publickey varchar(500)        -- 子节点公钥
);


-- 邀请码表
DROP TABLE IF EXISTS node_invitecode;
create table node_invitecode(
    invitecode varchar(100),
    createtime varchar(30),        -- 创建时间
    terminaltime varchar(30),      -- 终止时间
    status int              -- 状态，0 有效  1 已使用 2 终止 3 手工作废
);

-- 自身信息表
DROP TABLE IF EXISTS node_self;
create table node_self(
    nodeid varchar(100),        -- 节点id
    name varchar(100),          -- 节点名称
    ip varchar(100),            -- ip
    port int,           -- 端口
    nodetype varchar(30),      -- 节点类型 center 中心，relay 传输， deploy 部署
    deploytype varchar(30),    -- 部署类型 self 全人工决定，semi-auto 人工，auto 全自动
    area varchar(100),          -- 区域
    osname varchar(100),        -- 系统类别如window，linux
    osversion varchar(100),     -- 系统版本
    javaversion varchar(100),   -- java版本
    hostname varchar(100),      -- 操作系统名
    mac varchar(100),           -- mac地址
    parentid varchar(100),          -- 父节点唯一ID
    parentip varchar(100),          -- 父节点IP
    parentport int,        -- 父节点端口
    invitecode varchar(100),        -- 邀请码
    registertime varchar(30),      -- 注册时间
    lastconnecttime varchar(30),   -- 上次检测连接时间
    status  varchar(30)            -- 状态 0 作废 1 正用
);


-- 增量包列表
DROP TABLE IF EXISTS node_packlist;
create table node_packlist(
    no int,             -- 增量包序号
    filename varchar(100),      -- 文件名
    version varchar(100),       -- 版本号
    size varchar(100),          -- 大小(M)
    comment varchar(100),       -- 备注
    createtime varchar(30),    -- 创建时间
    releasetime varchar(30),   -- 发布时间
    assesstime varchar(30),    -- 预估更新时间
    downloadtime varchar(30),  -- 下载时间
    releasenum int,    -- 发布数(作废)
    updatenum int,     -- 已更新数(作废)
    area varchar(100),         -- 发布区域
    deploycount int,          -- 部署次数
    deploytime  varchar(30),   -- 上次部署时间
    status int         -- 状态 0 新增状态，未发布 1 正在更新  2 发布（更新完成）
);


-- 文件类别表
DROP TABLE IF EXISTS typelist;
create table typelist(
    typeid varchar(100),    -- 类别ID
    typename varchar(100)   -- 类别名称
);

-- 文件列表
DROP TABLE IF EXISTS filelist;
create table filelist(
    id varchar(100),            -- 文件号
    version varchar(100),       -- 版本号
    filename varchar(100),      -- 文件名
    size varchar(100),          -- 大小(M)
    comment varchar(100),       -- 备注
    createtime varchar(30),    -- 创建时间
    assesstime varchar(30),    -- 预估更新时间
    status int                 -- 状态 0 新增状态，未发布 1 正在更新  2 发布（更新完成）
);

-- 文件发布信息
DROP TABLE IF EXISTS releaselist;
create table releaselist(
    fileid varchar(100),       -- 文件ID
    typeid varchar(100),       -- 发布类别
    downloadnum varchar(100),  -- 下载次数
    index int,                 -- 序号
    releasetime varchar(30)    -- 发布时间
);