package bkoruznjak.from.hr.musae.player;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by bkoruznjak on 16/05/2017.
 */

public class PhoneStateHandler extends PhoneStateListener {

    WeakReference<PbPhoneStateListener> mPhoneStateListener;

    public PhoneStateHandler(WeakReference<PbPhoneStateListener> phoneStateListener) {
        this.mPhoneStateListener = phoneStateListener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        super.onCellInfoChanged(cellInfo);
    }

    @Override
    public void onDataActivity(int direction) {
        super.onDataActivity(direction);
        switch (direction) {
            case TelephonyManager.DATA_ACTIVITY_NONE:
                break;
            case TelephonyManager.DATA_ACTIVITY_IN:
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                break;
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);

        switch (serviceState.getState()) {
            case ServiceState.STATE_IN_SERVICE:
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                break;
            case ServiceState.STATE_POWER_OFF:
                break;
            default:
                break;
        }
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        PbPhoneStateListener phoneStateListener = mPhoneStateListener.get();
        if (phoneStateListener != null) {
            phoneStateListener.phoneStateChanged(state);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCellLocationChanged(CellLocation location) {
        super.onCellLocationChanged(location);
        if (location instanceof GsmCellLocation) {
            GsmCellLocation gcLoc = (GsmCellLocation) location;
        } else if (location instanceof CdmaCellLocation) {
            CdmaCellLocation ccLoc = (CdmaCellLocation) location;
        } else {
        }
    }

    @Override
    public void onCallForwardingIndicatorChanged(boolean cfi) {
        super.onCallForwardingIndicatorChanged(cfi);
    }

    @Override
    public void onMessageWaitingIndicatorChanged(boolean mwi) {
        super.onMessageWaitingIndicatorChanged(mwi);
    }
}