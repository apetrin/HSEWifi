package com.thermatk.android.l.hsewifi;
        import com.loopj.android.http.*;

        import android.annotation.TargetApi;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.Network;
        import android.net.NetworkCapabilities;
        import android.net.NetworkRequest;
        import android.net.wifi.WifiConfiguration;
        import android.net.wifi.WifiManager;
        import android.os.Build;
        import android.os.Handler;
        import android.os.IBinder;
        import android.util.Log;
        import android.widget.Toast;
        import org.apache.http.Header;

        import java.util.Iterator;
        import java.util.List;

public class MosMetro extends Service {
    private Handler handler;
    public void onCreate() {
        super.onCreate();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        Log.i("HSEWIFI", "4.B MOSMETRO SERVICE");
        sendInfo();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy() {
        super.onDestroy();
    }
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendInfo() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager connection_manager =
                    (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            connection_manager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAvailable(Network network) {
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            });
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("buttonClicked", "4");
        params.put("username", "mosmetro");
        params.put("password", "gfhjkm");
        params.put("redirect_url", "http://vmet.ro");
        params.put("err_flag", "0");
        Log.i("HSEWIFI", "5.B MOSMETRO SENDING REQUEST");
        client.post("http://1.1.1.1/login.html", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.authsent),
                                Toast.LENGTH_SHORT).show();
                    }

                });
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wm.setWifiEnabled(true)) {
                        List<WifiConfiguration> networks = wm.getConfiguredNetworks();
                        Iterator<WifiConfiguration> iterator = networks.iterator();
                        while (iterator.hasNext()) {
                            WifiConfiguration wifiConfig = iterator.next();
                            if (wifiConfig.SSID.equals("\"MosMetro_Free\""))
                                wm.enableNetwork(wifiConfig.networkId, true);
                            else
                                wm.disableNetwork(wifiConfig.networkId);
                        }
                        wm.reconnect();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("HSEWIFI", "6.B MOSMETRO FAILED REQUEST" + statusCode);
            }
        });
    }
}