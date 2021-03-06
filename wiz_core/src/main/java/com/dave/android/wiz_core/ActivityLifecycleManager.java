package com.dave.android.wiz_core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author rendawei
 * @date 2018/6/5
 */
public class ActivityLifecycleManager {

    private final Application application;
    private ActivityLifecycleManager.ActivityLifecycleCallbacksWrapper callbacksWrapper;

    public ActivityLifecycleManager(Context context) {
        application = (Application) context.getApplicationContext();
        if (VERSION.SDK_INT >= 14) {
            callbacksWrapper = new ActivityLifecycleManager.ActivityLifecycleCallbacksWrapper(application);
        }

    }

    public boolean registerCallbacks(ActivityLifecycleManager.Callbacks callbacks) {
        return this.callbacksWrapper != null && this.callbacksWrapper
                .registerLifecycleCallbacks(callbacks);
    }

    public void resetCallbacks() {
        if (this.callbacksWrapper != null) {
            this.callbacksWrapper.clearCallbacks();
        }

    }

    private static class ActivityLifecycleCallbacksWrapper {

        private final Set<ActivityLifecycleCallbacks> registeredCallbacks = new HashSet<>();
        private final Application application;

        ActivityLifecycleCallbacksWrapper(Application application) {
            this.application = application;
        }

        @TargetApi(14)
        private void clearCallbacks() {
            for (ActivityLifecycleCallbacks callback : this.registeredCallbacks) {
                this.application.unregisterActivityLifecycleCallbacks(callback);
            }
        }

        @TargetApi(14)
        private boolean registerLifecycleCallbacks(
                final ActivityLifecycleManager.Callbacks callbacks) {
            if (this.application != null) {
                ActivityLifecycleCallbacks callbackWrapper = new ActivityLifecycleCallbacks() {
                    public void onActivityCreated(Activity activity, Bundle bundle) {
                        callbacks.onActivityCreated(activity, bundle);
                    }

                    public void onActivityStarted(Activity activity) {
                        callbacks.onActivityStarted(activity);
                    }

                    public void onActivityResumed(Activity activity) {
                        callbacks.onActivityResumed(activity);
                    }

                    public void onActivityPaused(Activity activity) {
                        callbacks.onActivityPaused(activity);
                    }

                    public void onActivityStopped(Activity activity) {
                        callbacks.onActivityStopped(activity);
                    }

                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                        callbacks.onActivitySaveInstanceState(activity, bundle);
                    }

                    public void onActivityDestroyed(Activity activity) {
                        callbacks.onActivityDestroyed(activity);
                    }
                };
                this.application.registerActivityLifecycleCallbacks(callbackWrapper);
                this.registeredCallbacks.add(callbackWrapper);
                return true;
            } else {
                return false;
            }
        }
    }

    public abstract static class Callbacks {

        public Callbacks() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }
    }
}
