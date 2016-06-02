package edu.istic.tdf.dfclient.dagger.component;


import dagger.Component;
import edu.istic.tdf.dfclient.TdfApplication;
import edu.istic.tdf.dfclient.activity.BaseActivity;
import edu.istic.tdf.dfclient.activity.LoginActivity;
import edu.istic.tdf.dfclient.activity.MainMenuActivity;
import edu.istic.tdf.dfclient.activity.SitacActivity;
import edu.istic.tdf.dfclient.dagger.module.ActivitiesModule;
import edu.istic.tdf.dfclient.dagger.module.DaoModule;
import edu.istic.tdf.dfclient.dagger.module.FragmentsModule;
import edu.istic.tdf.dfclient.dagger.module.PushModule;
import edu.istic.tdf.dfclient.dagger.module.RestModule;

// TODO : Singleton fore some injections, not all of them
// TODO : Set that back
//@AppScope
//@Singleton
@Component(modules = {
        RestModule.class,
        DaoModule.class,
        ActivitiesModule.class,
        FragmentsModule.class,
        PushModule.class
})

public interface ApplicationComponent
{
    void inject(BaseActivity activity);
    void inject(LoginActivity activity);
    void inject(MainMenuActivity activity);
    void inject(SitacActivity activity);
    void inject(TdfApplication application);
}