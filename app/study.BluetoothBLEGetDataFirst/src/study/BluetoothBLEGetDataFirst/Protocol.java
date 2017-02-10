package study.BluetoothBLEGetDataFirst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

public class Protocol implements IoInterface{
    private BluetoothGatt mBluetoothGatt;
	
	OutputStream mOutputStream = null;
	InputStream mInputStream = null;
	static String strLog = "BluetoothChannelProtocol";
	static IoInterface io;

	static final int TYPE_NAVIGATION = 0x33;
	static final int ATTR_MOBILE_TO_OBU_NO_RSP = 0x7B;

	static final int ATTR_bbt_BYTE_INDEX = 0;
	static final int TYPE_bbt_BYTE_INDEX = 1;
	static final int LENGTH_bbt_BYTE_INDEX = 2;
	
	byte GET_LSB(int x) {
		return (byte) ((x) & 0xFF);
	}
	
	byte GET_NLSB(int x) {
		return (byte) (((x) & 0xFF00) >> 8);
	}

	byte GET_LONG_LSB(int x) {
		return (byte) ((x) & 0xFF);
	}

	byte GET_LONG_2SB(int x) {
		return (byte) (((x) & 0xFF00) >> 8);
	}

	byte GET_LONG_3SB(int x) {
		return (byte) (((x) & 0xFF0000) >> 16);
	}

	byte GET_LONG_NLSB(int x) {
		return (byte) (((x) & 0xFF000000) >> 24);
	}

	public Protocol(IoInterface iospp) {
		io = iospp;		
	}
	
	public boolean Protocol_Connect(String type, String travelName, String listcount, String count, int destY, int destX, String strName, String strAddr) throws UnsupportedEncodingException {
		Log.i(strLog, "run!!");
		
		byte[] bbtBuffer = new byte[2048];
		int bbtLen = 0, payLen = 0;
		final int StartIndex = 4;
		
		//宣告	
		byte[] bName = strName.getBytes("UTF8");
		Log.i(strLog, "length=" + bName.length);
		Log.i(strLog, "bName=" + Arrays.toString(bName));
		
		byte[] bAddr = strAddr.getBytes("UTF8");
		Log.i(strLog, "length=" + bAddr.length);
		Log.i(strLog, "bAddr=" + Arrays.toString(bAddr));

		byte[] btype = type.getBytes("UTF8");
		Log.i(strLog, "length=" + btype.length);
		Log.i(strLog, "btype=" + Arrays.toString(btype));

		byte[] btravelName = travelName.getBytes("UTF8");
		Log.i(strLog, "length=" + btravelName.length);
		Log.i(strLog, "btravelName=" + Arrays.toString(btravelName));

		byte[] blistcount = listcount.getBytes("UTF8");
		Log.i(strLog, "length=" + blistcount.length);
		Log.i(strLog, "blistcount=" + Arrays.toString(blistcount));

		byte[] bcount = count.getBytes("UTF8");
		Log.i(strLog, "length=" + bcount.length);
		Log.i(strLog, "bcount=" + Arrays.toString(bcount));

		bbtBuffer[ATTR_bbt_BYTE_INDEX] = (byte) ATTR_MOBILE_TO_OBU_NO_RSP;
		bbtBuffer[TYPE_bbt_BYTE_INDEX] = (byte) TYPE_NAVIGATION;
		bbtLen = 2;

		payLen = 0;	
		
		bbtBuffer[StartIndex + payLen] = GET_LONG_LSB(destY);
		bbtBuffer[StartIndex + payLen + 1] = GET_LONG_2SB(destY);
		bbtBuffer[StartIndex + payLen + 2] = GET_LONG_3SB(destY);
		bbtBuffer[StartIndex + payLen + 3] = GET_LONG_NLSB(destY);
		
		bbtBuffer[StartIndex + payLen + 4] = GET_LONG_LSB(destX);
		bbtBuffer[StartIndex + payLen + 5] = GET_LONG_2SB(destX);
		bbtBuffer[StartIndex + payLen + 6] = GET_LONG_3SB(destX);
		bbtBuffer[StartIndex + payLen + 7] = GET_LONG_NLSB(destX);

		payLen += 8;		
//		
		bbtBuffer[StartIndex + payLen] = GET_LSB(bName.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(bName.length);
		payLen += 2;

		System.arraycopy(bName, 0, bbtBuffer, StartIndex + payLen, bName.length);
		payLen += bName.length;
//
		bbtBuffer[StartIndex + payLen] = GET_LSB(bAddr.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(bAddr.length);
		payLen += 2;

		System.arraycopy(bAddr, 0, bbtBuffer, StartIndex + payLen, bAddr.length);
		payLen += bAddr.length;
		
//
		bbtBuffer[StartIndex + payLen] = GET_LSB(btype.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(btype.length);
		payLen += 2;

		System.arraycopy(btype, 0, bbtBuffer, StartIndex + payLen, btype.length);
		payLen += btype.length;
//	
		bbtBuffer[StartIndex + payLen] = GET_LSB(btravelName.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(btravelName.length);
		payLen += 2;

		System.arraycopy(btravelName, 0, bbtBuffer, StartIndex + payLen, btravelName.length);
		payLen += btravelName.length;
//	
		bbtBuffer[StartIndex + payLen] = GET_LSB(blistcount.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(blistcount.length);
		payLen += 2;

		System.arraycopy(blistcount, 0, bbtBuffer, StartIndex + payLen, blistcount.length);
		payLen += blistcount.length;
//	
		bbtBuffer[StartIndex + payLen] = GET_LSB(bcount.length);
		bbtBuffer[StartIndex + payLen + 1] = GET_NLSB(bcount.length);
		payLen += 2;

		System.arraycopy(bcount, 0, bbtBuffer, StartIndex + payLen, bcount.length);
		payLen += bcount.length;
		
		byte[] x = new byte[1];
		x[0] = 0 ;
		String test = null ;
		for(int i = 4 ; i <=payLen+3 ; i++){
			test = test+"\r\n"+"第"+String.valueOf(i)+"筆: "+String.valueOf(bbtBuffer[i]);
			
			x[0] = (byte) (x[0]+bbtBuffer[i]);
			Log.i("X:"+String.valueOf(i-4)+" ", Arrays.toString(x)) ;
		}
		Log.i("check", test) ;
		
		bbtBuffer[StartIndex + payLen ] = (byte) x[0];
		bbtBuffer[StartIndex + payLen + 1] = (byte) 0x7D;
		payLen += 2 ;

		bbtBuffer[StartIndex + payLen++] = 0;
		bbtBuffer[LENGTH_bbt_BYTE_INDEX] = GET_LSB(payLen-1);
		bbtBuffer[LENGTH_bbt_BYTE_INDEX + 1] = GET_NLSB(payLen);
		bbtLen = 4;
		bbtLen += payLen; 
		
		byte[] data = new byte[bbtLen-1];
		System.arraycopy(bbtBuffer, 0, data, 0, bbtLen-1);
		Log.i(strLog, "length=" + (bbtLen-1));
		Log.i(strLog, "data=" + Arrays.toString(data));
		
		io.IO_write(data, bbtLen);
		
		return false;
	}
	
	public boolean ProtocolTEST(String mod_data) throws UnsupportedEncodingException {
        byte[] send_data = mod_data.getBytes();
        
		io.IO_write2(send_data);
		
		return false;
	}
	
	@Override
	public int IO_write(byte[] data, int len) {
		try {
			mOutputStream.write(data);
		} catch (NullPointerException e) {
			// do something other
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int IO_write2(byte[] data) {
		// TODO Auto-generated method stub
		try {
			mOutputStream.write(data);
		} catch (NullPointerException e) {
			// do something other
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}	
}

interface IoInterface {
	public int IO_write(byte[] data,int len);
	public int IO_write2(byte[] data);
}
