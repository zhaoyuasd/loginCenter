package com.laozhao.store;

/*
   数据缓存接口  自定义实现方式 db redis local
   1.新增数据
   2.获取数据
   3.清除数据 包内  子类可见 定时清理
 */
public abstract class StoreManger {
    public abstract boolean addUserSession();
    public abstract UserSession getUserSession(String token);
    abstract void  cleanTimeOutSession();  //包内访问
}
