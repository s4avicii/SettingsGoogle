package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarrierConfigCache {
    protected static CarrierConfigManager sCarrierConfigManager;
    protected static final Map<Integer, PersistableBundle> sCarrierConfigs = new ConcurrentHashMap();
    private static CarrierConfigCache sInstance;
    private static final Object sInstanceLock = new Object();
    private static Map<Context, CarrierConfigCache> sTestInstances;

    public static CarrierConfigCache getInstance(Context context) {
        synchronized (sInstanceLock) {
            Map<Context, CarrierConfigCache> map = sTestInstances;
            if (map == null || !map.containsKey(context)) {
                CarrierConfigCache carrierConfigCache = sInstance;
                if (carrierConfigCache != null) {
                    return carrierConfigCache;
                }
                sInstance = new CarrierConfigCache();
                CarrierConfigChangeReceiver carrierConfigChangeReceiver = new CarrierConfigChangeReceiver();
                Context applicationContext = context.getApplicationContext();
                sCarrierConfigManager = (CarrierConfigManager) applicationContext.getSystemService(CarrierConfigManager.class);
                applicationContext.registerReceiver(carrierConfigChangeReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
                CarrierConfigCache carrierConfigCache2 = sInstance;
                return carrierConfigCache2;
            }
            CarrierConfigCache carrierConfigCache3 = sTestInstances.get(context);
            Log.w("CarrConfCache", "The context owner try to use a test instance:" + carrierConfigCache3);
            return carrierConfigCache3;
        }
    }

    public static void setTestInstance(Context context, CarrierConfigCache carrierConfigCache) {
        synchronized (sInstanceLock) {
            if (sTestInstances == null) {
                sTestInstances = new ConcurrentHashMap();
            }
            Log.w("CarrConfCache", "Try to set a test instance by context:" + context);
            sTestInstances.put(context, carrierConfigCache);
        }
    }

    private CarrierConfigCache() {
    }

    public boolean hasCarrierConfigManager() {
        return sCarrierConfigManager != null;
    }

    public PersistableBundle getConfigForSubId(int i) {
        if (sCarrierConfigManager == null) {
            return null;
        }
        Map<Integer, PersistableBundle> map = sCarrierConfigs;
        synchronized (map) {
            if (map.containsKey(Integer.valueOf(i))) {
                PersistableBundle persistableBundle = map.get(Integer.valueOf(i));
                return persistableBundle;
            }
            PersistableBundle configForSubId = sCarrierConfigManager.getConfigForSubId(i);
            if (configForSubId == null) {
                Log.e("CarrConfCache", "Could not get carrier config, subId:" + i);
                return null;
            }
            map.put(Integer.valueOf(i), configForSubId);
            return configForSubId;
        }
    }

    public PersistableBundle getConfig() {
        if (sCarrierConfigManager == null) {
            return null;
        }
        return getConfigForSubId(SubscriptionManager.getDefaultSubscriptionId());
    }

    private static class CarrierConfigChangeReceiver extends BroadcastReceiver {
        private CarrierConfigChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                Map<Integer, PersistableBundle> map = CarrierConfigCache.sCarrierConfigs;
                synchronized (map) {
                    if (SubscriptionManager.isValidSubscriptionId(intExtra)) {
                        map.remove(Integer.valueOf(intExtra));
                    } else {
                        map.clear();
                    }
                }
            }
        }
    }
}