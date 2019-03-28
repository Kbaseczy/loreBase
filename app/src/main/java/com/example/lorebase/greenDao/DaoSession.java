package com.example.lorebase.greenDao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.SearchHistory;
import com.example.lorebase.bean.ShareHistory;

import com.example.lorebase.greenDao.BrowseHistoryDao;
import com.example.lorebase.greenDao.SearchHistoryDao;
import com.example.lorebase.greenDao.ShareHistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig browseHistoryDaoConfig;
    private final DaoConfig searchHistoryDaoConfig;
    private final DaoConfig shareHistoryDaoConfig;

    private final BrowseHistoryDao browseHistoryDao;
    private final SearchHistoryDao searchHistoryDao;
    private final ShareHistoryDao shareHistoryDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        browseHistoryDaoConfig = daoConfigMap.get(BrowseHistoryDao.class).clone();
        browseHistoryDaoConfig.initIdentityScope(type);

        searchHistoryDaoConfig = daoConfigMap.get(SearchHistoryDao.class).clone();
        searchHistoryDaoConfig.initIdentityScope(type);

        shareHistoryDaoConfig = daoConfigMap.get(ShareHistoryDao.class).clone();
        shareHistoryDaoConfig.initIdentityScope(type);

        browseHistoryDao = new BrowseHistoryDao(browseHistoryDaoConfig, this);
        searchHistoryDao = new SearchHistoryDao(searchHistoryDaoConfig, this);
        shareHistoryDao = new ShareHistoryDao(shareHistoryDaoConfig, this);

        registerDao(BrowseHistory.class, browseHistoryDao);
        registerDao(SearchHistory.class, searchHistoryDao);
        registerDao(ShareHistory.class, shareHistoryDao);
    }
    
    public void clear() {
        browseHistoryDaoConfig.clearIdentityScope();
        searchHistoryDaoConfig.clearIdentityScope();
        shareHistoryDaoConfig.clearIdentityScope();
    }

    public BrowseHistoryDao getBrowseHistoryDao() {
        return browseHistoryDao;
    }

    public SearchHistoryDao getSearchHistoryDao() {
        return searchHistoryDao;
    }

    public ShareHistoryDao getShareHistoryDao() {
        return shareHistoryDao;
    }

}
