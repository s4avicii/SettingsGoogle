package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IElmyraServiceGestureListener extends IInterface {
    void onGestureDetected() throws RemoteException;

    void onGestureProgress(float f, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IElmyraServiceGestureListener {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
            }
            if (i != 1598968902) {
                if (i == 1) {
                    float readFloat = parcel.readFloat();
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onGestureProgress(readFloat, readInt);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onGestureDetected();
                }
                return true;
            }
            parcel2.writeString("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
            return true;
        }
    }
}
