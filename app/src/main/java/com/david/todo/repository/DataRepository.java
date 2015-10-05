package com.david.todo.repository;

import android.util.Log;

import com.david.todo.appstart.AndroidApplication;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by David on 06/09/2015.
 */
public class DataRepository {

    private AndroidApplication _applicationContext;

    public DataRepository(AndroidApplication applicationContext) {
        _applicationContext = applicationContext;
    }

    public void add(RealmObject realmObject) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(realmObject);
        realm.commitTransaction();
    }

    public RealmResults getAll(Class clazz) {
        return Realm.getDefaultInstance().where(clazz).findAll();
    }
}
