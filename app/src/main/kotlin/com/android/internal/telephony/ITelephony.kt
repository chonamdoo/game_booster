package com.android.internal.telephony

import android.os.*

/**
 * Created by chonamdoo on 2017. 6. 3..
 */

interface ITelephony : IInterface {

    abstract class Stub : Binder(), ITelephony {

        private class Proxy internal constructor(private val mRemote: IBinder) : ITelephony {

            override fun asBinder(): IBinder {
                return this.mRemote
            }

            val interfaceDescriptor: String
                get() = Stub.DESCRIPTOR

            @Throws(RemoteException::class)
            override fun endCall(): Boolean {
                var _result = true
                val _data = Parcel.obtain()
                val _reply = Parcel.obtain()
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR)
                    this.mRemote.transact(Stub.TRANSACTION_endCall, _data, _reply, 0)
                    _reply.readException()
                    if (_reply.readInt() == 0) {
                        _result = false
                    }
                    _reply.recycle()
                    _data.recycle()
                    return _result
                } catch (th: Throwable) {
                    _reply.recycle()
                    _data.recycle()
                }

                return _result

            }

            @Throws(RemoteException::class)
            override fun answerRingingCall() {
                val _data = Parcel.obtain()
                val _reply = Parcel.obtain()
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR)
                    this.mRemote.transact(Stub.TRANSACTION_answerRingingCall, _data, _reply, 0)
                    _reply.readException()
                } finally {
                    _reply.recycle()
                    _data.recycle()
                }
            }

            @Throws(RemoteException::class)
            override fun enableDataConnectivity(): Boolean {
                var _result = false
                val _data = Parcel.obtain()
                val _reply = Parcel.obtain()
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR)
                    this.mRemote.transact(Stub.TRANSACTION_enableDataConnectivity, _data, _reply, 0)
                    _reply.readException()
                    if (_reply.readInt() != 0) {
                        _result = true
                    }
                    _reply.recycle()
                    _data.recycle()
                    return _result
                } catch (th: Throwable) {
                    _reply.recycle()
                    _data.recycle()
                }

                return _result

            }

            @Throws(RemoteException::class)
            override fun disableDataConnectivity(): Boolean {
                var _result = false
                val _data = Parcel.obtain()
                val _reply = Parcel.obtain()
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR)
                    this.mRemote.transact(Stub.TRANSACTION_disableDataConnectivity, _data, _reply, 0)
                    _reply.readException()
                    if (_reply.readInt() != 0) {
                        _result = true
                    }
                    _reply.recycle()
                    _data.recycle()
                    return _result
                } catch (th: Throwable) {
                    _reply.recycle()
                    _data.recycle()
                }

                return _result

            }

            override val isDataConnectivityPossible: Boolean
                @Throws(RemoteException::class)
                get() {
                    var _result = false
                    val _data = Parcel.obtain()
                    val _reply = Parcel.obtain()
                    try {
                        _data.writeInterfaceToken(Stub.DESCRIPTOR)
                        this.mRemote.transact(Stub.TRANSACTION_isDataConnectivityPossible, _data, _reply, 0)
                        _reply.readException()
                        if (_reply.readInt() != 0) {
                            _result = true
                        }
                        _reply.recycle()
                        _data.recycle()
                        return _result
                    } catch (th: Throwable) {
                        _reply.recycle()
                        _data.recycle()
                    }

                    return _result

                }
        }

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder {
            return this
        }

        @Throws(RemoteException::class)
        public override fun onTransact(code: Int, data: Parcel, reply: Parcel, flags: Int): Boolean {
            var i = 0
            val _result: Boolean
            when (code) {
                TRANSACTION_endCall /*1*/ -> {
                    data.enforceInterface(DESCRIPTOR)
                    _result = endCall()
                    reply.writeNoException()
                    if (_result) {
                        i = TRANSACTION_endCall
                    }
                    reply.writeInt(i)
                    return true
                }
                TRANSACTION_answerRingingCall /*2*/ -> {
                    data.enforceInterface(DESCRIPTOR)
                    answerRingingCall()
                    reply.writeNoException()
                    return true
                }
                TRANSACTION_enableDataConnectivity /*3*/ -> {
                    data.enforceInterface(DESCRIPTOR)
                    _result = enableDataConnectivity()
                    reply.writeNoException()
                    if (_result) {
                        i = TRANSACTION_endCall
                    }
                    reply.writeInt(i)
                    return true
                }
                TRANSACTION_disableDataConnectivity /*4*/ -> {
                    data.enforceInterface(DESCRIPTOR)
                    _result = disableDataConnectivity()
                    reply.writeNoException()
                    if (_result) {
                        i = TRANSACTION_endCall
                    }
                    reply.writeInt(i)
                    return true
                }
                TRANSACTION_isDataConnectivityPossible /*5*/ -> {
                    data.enforceInterface(DESCRIPTOR)
                    _result = isDataConnectivityPossible
                    reply.writeNoException()
                    if (_result) {
                        i = TRANSACTION_endCall
                    }
                    reply.writeInt(i)
                    return true
                }
                1598968902 -> {
                    reply.writeString(DESCRIPTOR)
                    return true
                }
                else -> return super.onTransact(code, data, reply, flags)
            }
        }

        companion object {
            private val DESCRIPTOR = "com.android.internal.telephony.ITelephony"
            internal val TRANSACTION_answerRingingCall = 2
            internal val TRANSACTION_disableDataConnectivity = 4
            internal val TRANSACTION_enableDataConnectivity = 3
            internal val TRANSACTION_endCall = 1
            internal val TRANSACTION_isDataConnectivityPossible = 5

            fun asInterface(obj: IBinder?): ITelephony? {
                if (obj == null) {
                    return null
                }
                val iin = obj.queryLocalInterface(DESCRIPTOR)
                if (iin == null || iin !is ITelephony) {
                    return Proxy(obj)
                }
                return iin
            }
        }
    }

    @Throws(RemoteException::class)
    fun answerRingingCall()

    @Throws(RemoteException::class)
    fun disableDataConnectivity(): Boolean

    @Throws(RemoteException::class)
    fun enableDataConnectivity(): Boolean

    @Throws(RemoteException::class)
    fun endCall(): Boolean

    val isDataConnectivityPossible: Boolean
}
