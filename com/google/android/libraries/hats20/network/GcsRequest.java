package com.google.android.libraries.hats20.network;

import android.net.Uri;
import android.os.Build;
import com.google.android.libraries.hats20.storage.HatsDataStore;
import java.util.Locale;

public class GcsRequest {
    public static final String USER_AGENT = String.format(Locale.US, "Mozilla/5.0; Hats App/v%d (Android %s; SDK %d; %s; %s; %s)", new Object[]{2, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT), Build.ID, Build.MODEL, Build.TAGS});
    private final HatsDataStore hatsDataStore;
    private final String postData;
    private final Uri requestUriWithNoParams;
    private final ResponseListener responseListener;

    public interface ResponseListener {
        void onError(Exception exc);

        void onSuccess(GcsResponse gcsResponse);
    }

    public GcsRequest(ResponseListener responseListener2, Uri uri, HatsDataStore hatsDataStore2) {
        this.responseListener = responseListener2;
        this.postData = uri.getEncodedQuery();
        this.requestUriWithNoParams = uri.buildUpon().clearQuery().build();
        this.hatsDataStore = hatsDataStore2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x010e  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void send() {
        /*
            r8 = this;
            java.lang.String r0 = "utf-8"
            r1 = 0
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ IOException | JSONException -> 0x0100 }
            java.net.URL r4 = new java.net.URL     // Catch:{ IOException | JSONException -> 0x0100 }
            android.net.Uri r5 = r8.requestUriWithNoParams     // Catch:{ IOException | JSONException -> 0x0100 }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException | JSONException -> 0x0100 }
            r4.<init>(r5)     // Catch:{ IOException | JSONException -> 0x0100 }
            java.net.URLConnection r4 = r4.openConnection()     // Catch:{ IOException | JSONException -> 0x0100 }
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ IOException | JSONException -> 0x0100 }
            r1 = 1
            r4.setDoOutput(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1 = 0
            r4.setInstanceFollowRedirects(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r5 = "POST"
            r4.setRequestMethod(r5)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r5 = "Content-Type"
            java.lang.String r6 = "application/x-www-form-urlencoded"
            r4.setRequestProperty(r5, r6)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r5 = r8.postData     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            byte[] r5 = r5.getBytes(r0)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r6 = "Content-Length"
            int r7 = r5.length     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r7 = java.lang.Integer.toString(r7)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r4.setRequestProperty(r6, r7)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r6 = "charset"
            r4.setRequestProperty(r6, r0)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r0 = "Connection"
            java.lang.String r6 = "close"
            r4.setRequestProperty(r0, r6)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r0 = "User-Agent"
            java.lang.String r6 = USER_AGENT     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r4.setRequestProperty(r0, r6)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r4.setUseCaches(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.DataOutputStream r0 = new java.io.DataOutputStream     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.OutputStream r1 = r4.getOutputStream()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r0.<init>(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r0.write(r5)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.InputStream r5 = r4.getInputStream()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1.<init>(r5)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r0.<init>(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1.<init>()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
        L_0x0072:
            java.lang.String r5 = r0.readLine()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            if (r5 == 0) goto L_0x007c
            r1.append(r5)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            goto L_0x0072
        L_0x007c:
            r0.close()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            long r5 = r5 - r2
            java.lang.String r0 = r1.toString()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r1 = "HatsLibGcsRequest"
            int r2 = r0.length()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r3 = 55
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r7.<init>(r3)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r3 = "Downloaded "
            r7.append(r3)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r7.append(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r2 = " bytes in "
            r7.append(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r7.append(r5)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r2 = " ms"
            r7.append(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r2 = r7.toString()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            android.util.Log.d(r1, r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            boolean r1 = r0.isEmpty()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            if (r1 == 0) goto L_0x00c3
            com.google.android.libraries.hats20.network.GcsRequest$ResponseListener r1 = r8.responseListener     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.io.IOException r2 = new java.io.IOException     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r3 = "GCS responded with no data. The site's publishing state may not be Enabled. Check Site > Advanced settings > Publishing state. For more info, see go/get-hats"
            r2.<init>(r3)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1.onError(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
        L_0x00c3:
            com.google.android.libraries.hats20.storage.HatsDataStore r1 = r8.hatsDataStore     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            android.net.Uri r2 = r8.requestUriWithNoParams     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.util.Map r3 = r4.getHeaderFields()     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1.storeSetCookieHeaders(r2, r3)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r1.<init>(r0)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r2 = "params"
            org.json.JSONObject r1 = r1.getJSONObject(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r2 = "responseCode"
            int r2 = r1.getInt(r2)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            java.lang.String r3 = "expirationDate"
            long r5 = r1.getLong(r3)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            com.google.android.libraries.hats20.network.GcsResponse r1 = new com.google.android.libraries.hats20.network.GcsResponse     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            if (r2 != 0) goto L_0x00ea
            goto L_0x00ec
        L_0x00ea:
            java.lang.String r0 = ""
        L_0x00ec:
            r1.<init>(r2, r5, r0)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            com.google.android.libraries.hats20.network.GcsRequest$ResponseListener r0 = r8.responseListener     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r0.onSuccess(r1)     // Catch:{ IOException | JSONException -> 0x00fb, all -> 0x00f8 }
            r4.disconnect()
            goto L_0x010b
        L_0x00f8:
            r8 = move-exception
            r1 = r4
            goto L_0x010c
        L_0x00fb:
            r0 = move-exception
            r1 = r4
            goto L_0x0101
        L_0x00fe:
            r8 = move-exception
            goto L_0x010c
        L_0x0100:
            r0 = move-exception
        L_0x0101:
            com.google.android.libraries.hats20.network.GcsRequest$ResponseListener r8 = r8.responseListener     // Catch:{ all -> 0x00fe }
            r8.onError(r0)     // Catch:{ all -> 0x00fe }
            if (r1 == 0) goto L_0x010b
            r1.disconnect()
        L_0x010b:
            return
        L_0x010c:
            if (r1 == 0) goto L_0x0111
            r1.disconnect()
        L_0x0111:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.hats20.network.GcsRequest.send():void");
    }
}
