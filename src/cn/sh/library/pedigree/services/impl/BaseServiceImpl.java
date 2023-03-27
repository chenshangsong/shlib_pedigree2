package cn.sh.library.pedigree.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.BaseService;

/**
 * Created by ly on 2014-10-17.
 */
@Service
public class BaseServiceImpl implements BaseService {

    protected <T> T transform(List<Object> list, Class<T> clazz){
        try {
            T t = clazz.newInstance();
            return t;
        } catch( Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
