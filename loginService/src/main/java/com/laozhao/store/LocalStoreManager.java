package com.laozhao.store;

import com.laozhao.store.StoreManger;
import com.laozhao.store.UserSession;
import org.springframework.stereotype.Service;

/*
  数据缓存接口
 */
@Service
public class LocalStoreManager extends StoreManger {
    @Override
    public boolean addUserSession() {
        return false;
    }

    @Override
    public UserSession getUserSession(String  token) {
        return null;
    }

    @Override
    void cleanTimeOutSession() {

    }


}
