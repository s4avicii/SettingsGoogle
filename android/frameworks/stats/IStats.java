package android.frameworks.stats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStats extends IInterface {
    public static final String DESCRIPTOR = "android$frameworks$stats$IStats".replace('$', '.');

    void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException;

    public static abstract class Stub extends Binder implements IStats {
        public static IStats asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IStats.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStats)) {
                return new Proxy(iBinder);
            }
            return (IStats) queryLocalInterface;
        }

        private static class Proxy implements IStats {
            private String mCachedHash = "-1";
            private int mCachedVersion = -1;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStats.DESCRIPTOR);
                    obtain.writeTypedObject(vendorAtom, 0);
                    if (!this.mRemote.transact(1, obtain, (Parcel) null, 1)) {
                        throw new RemoteException("Method reportVendorAtom is unimplemented.");
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
