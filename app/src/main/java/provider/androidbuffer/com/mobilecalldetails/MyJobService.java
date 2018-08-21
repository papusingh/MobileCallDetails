package provider.androidbuffer.com.mobilecalldetails;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.CallLog;
import android.util.Log;
import java.util.Date;

/**
 * Created by incred-dev on 21/8/18.
 */

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCallLogDetails(MyJobService.this);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @SuppressLint("MissingPermission")
    public static void getCallLogDetails(Context context) {
        Cursor cursor = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor != null){
            Log.d("cursor", DatabaseUtils.dumpCursorToString(cursor));
            while (cursor.moveToNext()){
                String dir = null;
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                if (name == null){
                    name = "Unknown";
                }
                Log.d("service name",name);
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                Log.d("service number",number);

                String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                int callTypeCode = Integer.parseInt(callType);
                switch (callTypeCode){
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "Missed Call";
                        break;
                }

                Log.d("service type",dir+"");

                String dt = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                Date date = new Date(Long.valueOf(dt));
                Log.d("service date",date.toString());

                String callDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                Log.d("service call duration",callDuration);
            }
        }
        cursor.close();
    }

}
