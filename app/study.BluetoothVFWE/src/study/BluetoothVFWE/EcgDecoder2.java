package study.BluetoothVFWE;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Handler;

public class EcgDecoder2 {
	
//	Handler mHandler;
//	MedBuffer2 mMedBuffer;
//	SendSQLBuffer mSendSQLBuffer;
//	SimpleDateFormat formatter;
//	String macAddress,hostIP;
//	boolean ecg_key = false;
//	public EcgDecoder2(Handler handler,MedBuffer2 mMedBuffer,SendSQLBuffer mSendSQLBuffer) {
//		this.mHandler=handler;
//		this.mMedBuffer=mMedBuffer;
//		this.mSendSQLBuffer = mSendSQLBuffer;
//	}
//	/*初始化擷取心跳率計算*/
//	int[] QRS_buffer=new int[10] ;
//	int QRS_count = 0;
//	long QRS_time = 0;
//	long LAST_QRS_time = 0;
//	int[] f = new int [30];
//	int  QRS_flag = 0;
//	float frequency=0;
//	int f1=0,f2=0,i=0;
//	int count = 0; //傳送至資料庫 每五筆送一筆
//	int getc = 0;
//	int x = 1;
//	int j = 3;
//	long ucTemp;
//	int sum_1,sum_2,sum_3,check_sum;
//	int sum,final_Division;
//	public void setSendInfo(SimpleDateFormat formatter,String macAddress,String hostIP) {
//		this.formatter = formatter;
//		this.macAddress = macAddress;
//		this.hostIP = hostIP;
//	}
//	void OnBeep() {
//
//
//		QRS_time = System.currentTimeMillis();		//取得此時系統計數器，count值的單位為mS
//		frequency = QRS_time - LAST_QRS_time;	//此時系統計數減去上次系統計數，得到心跳的間隔時間
//		frequency = 1000/frequency*60;		//將時間轉換為頻率
//		f1=(int)frequency;
//		f[29]=(int)frequency;
//		if(f[0]>0) {						//若f[0]大於"0"，則表示平均心跳率的運算暫存器已經取滿
//			for(i=0; i<30; i++)
//				f2=f2+f[i];					//將平均心跳率的運算暫存器加總並平均
//			f2=(int) ((f2/30) + 0.5);
////    		mHandler.obtainMessage(Medical_Monitor.MESSAGE_HEART_RATE, f2 ,-1 , null).sendToTarget();
//		} else {
//			f2=0;
//		}
//		LAST_QRS_time = QRS_time;
//		//Beep(2500,100);						//呼叫系統音效
//		for(i=0; i<29; i++)					//移動平均心跳率的運算暫存器
//			f[i]=f[i+1];
//	}
//	void QRS_Detection(int QRS_buffer[]) {
//		int QRS_SUM;
//		if(QRS_count>50) {		//偵測到QRS後，DELAY 50 個simple再繼續偵測，以節省程式資源
//			if(QRS_buffer[0]>0 ) {	//判斷是否已填滿需運算的暫存器
//				QRS_SUM=(QRS_buffer[3]-QRS_buffer[2])+(QRS_buffer[3]-QRS_buffer[1])/2+(QRS_buffer[3]-QRS_buffer[0])/3;
//				//統計平均斜率
//				if(QRS_SUM>40 && QRS_SUM<300) {		//若平均斜率在此範圍之內
//					//則視為QRS上升中
//					QRS_flag=1;							//將上升中旗標設為"1"
//				} else {
//					if(QRS_flag==1) {					//斜率不在QRS上升範圍內
//						//若此時QRS上升旗標為"1"，即表示上個狀態斜率為QRS上升
//						QRS_count=0;					//因此，此時波形的狀態為QRS第1次下降，而在此兩點間即為QRS的尖峰
//						OnBeep();//呼叫系統音效執行緒
//					}
//					QRS_flag=0;							//使QRS上升旗標為"0"
//				}
//			}
//		} else {
//			QRS_count++;
//		}
//		for(int i=0; i<3; i++) {						//移動QRS偵測暫存器
//			QRS_buffer[i]=QRS_buffer[i+1];
//		}
//	}
//	public int analyze(byte buffer2,byte buffer3,byte checksum) {
//		sum_1 = ASCII.ascii(sum_1);
//		sum_1 = buffer2 & 0xFF;
//		sum_2 = ASCII.ascii(sum_2);
//		sum_2 = buffer3 & 0xFF;
//		check_sum=checksum & 0xFF;
//		ucTemp=sum_1+sum_2;
//		if(ucTemp>255) {
//			ucTemp=ucTemp%256;
//		}
//		if(ucTemp==check_sum) {
//			x = Medical_Monitor2.ggg();
//			if(x==1)getc=0;
//			else getc=getc+1;
//			if(getc==(x-1)) {
//				sum = sum_1;
//				final_Division = sum ;
//				getc = 0;
//				QRS_buffer[3] = final_Division;
//				QRS_Detection(QRS_buffer);
//				mHandler.obtainMessage(Medical_Monitor2.MESSAGE_HEART_RATE, sum_2 ,0 , null).sendToTarget();
//				if(ecg_key == false) {
//					Medical_Monitor2.aaa(1);
//					mHandler.obtainMessage(Medical_Monitor2.MESSAGE_HEART_RATE, 0 ,-1 , null).sendToTarget();
//					ecg_key = true;
//				}
//				j = Medical_Monitor2.jhj();
//				mMedBuffer.savedata(sum_1*(j+1),1);
//				//存入sql的專用傳輸Buffer
//				String now = formatter.format(new Date());
//				String sendSQL = "Vic,ECG,E"+String.valueOf(final_Division)+","+"WAY"+","+now+","+"DataAndroid"+","+String.valueOf(f2)+",,,,,"+hostIP+",";
//				mSendSQLBuffer.addFirst(sendSQL);
//			}
//			return final_Division;
//		} else
//			return final_Division;
//	}
}
