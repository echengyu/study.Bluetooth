package study.BluetoothBLEGetDataFirst;

import android.os.Bundle;
import android.os.Message;

public class Packet {
	
	static int Code_in_Frist=0;
	//private StringBuffer sMsgofCmdPID = new StringBuffer(128);
    static byte [] aMsgofCmdPID;
    static byte [] aMsgofCmdPID_2;
    
	static int COOLANT_sum = -1;
	static int RPM_sum = -1;
	static int SPEED_sum = -1;
	static int FRP_sum = -1;
	static int CLV_sum = -1;
    static int COOLANT_data01 = -1;
    static int COOLANT_data02 = -1;
    static int RPM_data01 = -1,RPM_data02 = -1,RPM_data03 = -1,RPM_data04 = -1;
    static int SPEED_data01 = -1,SPEED_data02 = -1;
    static int FRP_data01 = -1,FRP_data02 = -1;
    static int CLV_data01 = -1,CLV_data02 = -1;
    
    static int BUG_0_0,BUG_0_1,BUG_1_0,BUG_1_1,BUG_1_2,BUG_2_0;
    static int data_sum,data01,data02,data03,data04;
    
	  //static int ID1,ID2,ID3,ID4;

    
    static byte [] Mode = new byte[2];
    static byte [] PID  = new byte[2];
    
    static byte [] Check_7E8  = new byte[3];
    static byte [] canlen  = new byte[2];
    static byte [] Mult  = new byte[2];
    static int Int_Mult ;
    
    static byte [] Length  = new byte[2];
    static int Int_Length ;
    
    static byte [] R_Mode  = new byte[2];
    static byte [] R_PID  = new byte[2];
    static byte [] RSP  = new byte[4];
    static byte [] Data_Packet  = new byte[100];
    
    static Message mMsg = new Message();
    static Bundle data = new Bundle();
    
    static Message mmMsg = new Message();
    static Bundle mmdata = new Bundle();
    
	public static Message Filter(StringBuffer sMsgofCmdPID,int mCode_in_Frist) {
		
		aMsgofCmdPID = sMsgofCmdPID.toString().getBytes();
		Code_in_Frist = mCode_in_Frist;
		
		try{
			/*
			System.arraycopy(aMsgofCmdPID,15,zzzz,0,4);//嚙稷嚙踝蕭byte[]
			return zzzz;
			*/
			//System.arraycopy(aMsgofCmdPID, 4, zzzz, 0, 5) ;
			//return zzzz;
			//aMsgofCmdPID_2[0] = aMsgofCmdPID[15];
			//aMsgofCmdPID_2[1] = aMsgofCmdPID[16];
			//aMsgofCmdPID_2[2] = aMsgofCmdPID[17];
			//aMsgofCmdPID_2[3] = aMsgofCmdPID[18];
			/*
			if(aMsgofCmdPID[15] >= 65)				//嚙緬嚙踝蕭X嚙碼嚙踝蕭嚙踝蕭嚙踝蕭J嚙碼
				data01 = aMsgofCmdPID[15] - 55;
			else
				data01 = aMsgofCmdPID[15] - 48;
			if(aMsgofCmdPID[16] >= 65)
				data02 = aMsgofCmdPID[16] - 55;
			else
				data02 = aMsgofCmdPID[16] - 48;
			if(aMsgofCmdPID[17] >= 65)
				data03 = aMsgofCmdPID[17] - 55;
			else
				data03 = aMsgofCmdPID[17] - 48;
			if(aMsgofCmdPID[18] >= 65)
				data04 = aMsgofCmdPID[18] - 55;
			else
				data04 = aMsgofCmdPID[18] - 48;
			data_sum = data01*4096 + data02*256 + data03*16 + data04;		
			//return data_sum;
			switch(data_sum)
			{
				case 0x0105:
					System.arraycopy(aMsgofCmdPID,42,zzzz,0,2);//嚙稷嚙踝蕭byte[]
					return zzzz;
			}
			*/
			/*
			
			System.arraycopy(aMsgofCmdPID,15,Lenght,0,2);//嚙稷嚙踝蕭byte[]
			System.arraycopy(aMsgofCmdPID,17,PID,0,2);//嚙稷嚙踝蕭byte[]
			*/
			switch(Code_in_Frist)//嚙瞑嚙稻CAN嚙踝蕭CAN嚙瘡嚙羯嚙踝蕭Protocol
			{
				case 1:
				case 2://嚙瞌CAN
				case 3:
				case 4:
					try{
					System.arraycopy(aMsgofCmdPID,15,Mode,0,2);	//嚙稷嚙踝蕭Mode 
					System.arraycopy(aMsgofCmdPID,17,PID,0,2);	//嚙稷嚙踝蕭PID
					System.arraycopy(aMsgofCmdPID,25,Check_7E8,0,3);	// CAN 嚙稷嚙踝蕭Check_7E8
					System.arraycopy(aMsgofCmdPID,29,canlen,0,2);	// CAN 嚙稷嚙踝蕭Check_7E8
//					switch( ByteArray3_To_Int(Check_7E8) )
//					switch(ByteArray3_To_Int(canlen))
//					{
//						case 0x7E8:
//						case 0x08:	
							switch(aMsgofCmdPID[16]) //嚙瞑嚙稻Mode
							{
								case '1':	//嚙課佗蕭1
								case '6':	//嚙課佗蕭6
								case '9':	//嚙課佗蕭9
									System.arraycopy(aMsgofCmdPID,32,Mult,0,2);
									Int_Mult = ByteArray2_To_Int(Mult) ;
									
									switch( Int_Mult )//嚙瞑嚙稻嚙瞌嚙稻嚙篁嚙褊包
									{
										case 0x03:
										case 0x04:
										case 0x05:	//嚙踝蕭吤]
										case 0x06:
										case 0x07:
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,32,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,34,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,36,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
											System.arraycopy(aMsgofCmdPID,38,Data_Packet,0, ( (Int_Mult-2)*2) );		//嚙稷嚙踝蕭Data
											
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
											
											return mMsg;
											
										case 0x10://嚙篁嚙褊包
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,34,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,36,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,38,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
											
											Int_Length = ByteArray2_To_Int(Length);
											if(Int_Length <= 0x06 )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , ((Int_Length-2)*2) );
											}
											else if(Int_Length <= 0x0D )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , ((Int_Length-6)*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x0D )
											else if(Int_Length <= 0x14 )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 22 , ((Int_Length - 0x0D )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												
											}//end of else if(Int_Length <= 0x14 )
											
											else if(Int_Length <= 0x1B )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 22 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[182] == '2')
													{
														if( aMsgofCmdPID[183] == '3' )
														{
															System.arraycopy(aMsgofCmdPID,184,Data_Packet, 36 , ((Int_Length - 0x14 )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												
											}//end of else if(Int_Length <= 0x1B )
											
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
												
											return mMsg;
											
									}
									
									break;
								case '2':	//嚙課佗蕭2
									System.arraycopy(aMsgofCmdPID,32,Mult,0,2);
									Int_Mult = ByteArray2_To_Int(Mult) ;
									switch( Int_Mult )//嚙瞑嚙稻嚙瞌嚙稻嚙篁嚙褊包
									{
										case 0x04:
										case 0x05:	//嚙踝蕭吤]
										case 0x06:
										case 0x07:
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,32,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,34,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,36,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
											System.arraycopy(aMsgofCmdPID,40,Data_Packet,0, ( (Int_Mult-3)*2) );		//嚙稷嚙踝蕭Data
											
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
											
											return mMsg;
											
										case 0x10://嚙篁嚙褊包
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,34,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,36,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,38,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
											Int_Length = ByteArray2_To_Int(Length);
											if(Int_Length <= 0x06 )
											{
												System.arraycopy(aMsgofCmdPID,42,Data_Packet, 0 , ((Int_Length-3)*2) );
											}
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x0D )
											{
												System.arraycopy(aMsgofCmdPID,42,Data_Packet, 0 , 6 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 6 , ((Int_Length-6)*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x0D )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x14 )
											{
												System.arraycopy(aMsgofCmdPID,42,Data_Packet, 0 , 6 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 6 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 20 , ((Int_Length - 0x0D )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												
											}//end of else if(Int_Length <= 0x14 )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x1B )
											{
												System.arraycopy(aMsgofCmdPID,42,Data_Packet, 0 , 6 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 6 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 20 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '3' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 34 , ((Int_Length - 0x14 )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												
											}//end of else if(Int_Length <= 0x1B )
											//----------------------------------------------------------------------------------------------------
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
											
											return mMsg;
											
									}
									
									
									break;
								case '3':	//嚙課佗蕭3
									System.arraycopy(aMsgofCmdPID,32,Mult,0,2);
									Int_Mult = ByteArray2_To_Int(Mult) ;
									switch( Int_Mult )//嚙瞑嚙稻嚙瞌嚙稻嚙篁嚙褊包
									{
										case 0x01:
										case 0x02:
										case 0x04://嚙踝蕭吤]
										case 0x06:
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,32,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,34,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,38,Data_Packet,0, ( (Int_Mult-2)*2) );//嚙稷嚙踝蕭Data
											
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
											
											return mMsg;
											
										case 0x10://嚙篁嚙褊包
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,34,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,36,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											Int_Length = ByteArray2_To_Int(Length);
											if(Int_Length <= 0x06 )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , ((Int_Length-2)*2) );
											}
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x0D )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , ((Int_Length-6)*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x0D )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x14 )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 22 , ((Int_Length - 0x0D )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x14 )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x1B )
											{
												System.arraycopy(aMsgofCmdPID,40,Data_Packet, 0 , 8 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 8 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 22 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[182] == '2')
													{
														if( aMsgofCmdPID[183] == '3' )
														{
															System.arraycopy(aMsgofCmdPID,184,Data_Packet, 36 , ((Int_Length - 0x14 )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x1B )
											//----------------------------------------------------------------------------------------------------
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
												
											return mMsg;
											
									}
									
									
									break;
								case '4':	//嚙課佗蕭4
									//嚙瞎嚙踝蕭嚙踝蕭~嚙碼嚙璀嚙磅嚙瘤嚙踝蕭
									break;
								case '5':	//嚙課佗蕭5
									//CAN嚙盤Mode5
									break;
								case '7':
									System.arraycopy(aMsgofCmdPID,32,Mult,0,2);
									Int_Mult = ByteArray2_To_Int(Mult) ;
									switch( Int_Mult )//嚙瞑嚙稻嚙瞌嚙稻嚙篁嚙褊包
									{
										case 0x02:
										case 0x03:
										case 0x04://嚙踝蕭吤]
										case 0x05:
										case 0x06:
										case 0x07:
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,32,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,34,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											System.arraycopy(aMsgofCmdPID,36,Data_Packet,0, ( (Int_Mult-1)*2) );//嚙稷嚙踝蕭Data
											
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
											
											return mMsg;
											
										case 0x10://嚙篁嚙褊包
											
											clean_ByteArray();
											
											System.arraycopy(aMsgofCmdPID,34,Length,0,2);	//嚙稷嚙踝蕭Length
											System.arraycopy(aMsgofCmdPID,36,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
											Int_Length = ByteArray2_To_Int(Length);
											if(Int_Length <= 0x06 )
											{
												System.arraycopy(aMsgofCmdPID,38,Data_Packet, 0 , ((Int_Length-1)*2) );
											}
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x0D )
											{
												System.arraycopy(aMsgofCmdPID,38,Data_Packet, 0 , 10 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 10 , ((Int_Length-6)*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x0D )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x14 )
											{
												System.arraycopy(aMsgofCmdPID,38,Data_Packet, 0 , 10 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 10 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 24 , ((Int_Length - 0x0D )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x14 )
											//----------------------------------------------------------------------------------------------------
											else if(Int_Length <= 0x1B )
											{
												System.arraycopy(aMsgofCmdPID,38,Data_Packet, 0 , 10 );
												try
												{
													if(aMsgofCmdPID[82] == '2')
													{
														if( aMsgofCmdPID[83] == '1' )
														{
															System.arraycopy(aMsgofCmdPID,84,Data_Packet, 10 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[132] == '2')
													{
														if( aMsgofCmdPID[133] == '2' )
														{
															System.arraycopy(aMsgofCmdPID,134,Data_Packet, 24 , 14 );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
												try
												{
													if(aMsgofCmdPID[182] == '2')
													{
														if( aMsgofCmdPID[183] == '3' )
														{
															System.arraycopy(aMsgofCmdPID,184,Data_Packet, 38 , ((Int_Length - 0x14 )*2) );
														}
													}
												}catch (Exception e){
													e.printStackTrace();
												}//end of try
											}//end of else if(Int_Length <= 0x1B )
											//----------------------------------------------------------------------------------------------------
											mMsg = new Message() ;
											data.putByteArray("Length", Length);
											data.putByteArray("R_Mode", R_Mode);
											data.putByteArray("R_PID", R_PID);
											data.putByteArray("Data_Packet", Data_Packet);
											mMsg.setData(data);
												
											return mMsg;
											
									}
									
									break;
									
									
								
							}//end of switch(aMsgofCmdPID[16]) //嚙瞑嚙稻Mode
//					}//end of switch( Byte3_To_Int(Check_7E8) )
					
					}catch (Exception e){
						e.printStackTrace();
						
						clean_ByteArray();
						
						mMsg = new Message() ;
						Bundle data = new Bundle();
						data.putByteArray("Length", Length);
						data.putByteArray("R_Mode", R_Mode);
						data.putByteArray("R_PID", R_PID);
						data.putByteArray("Data_Packet", Data_Packet);
						mMsg.setData(data);
						return mMsg;
						
					}//end of try;
					
					
					
				case 5:
				case 6://嚙瞌CAN嚙瘡嚙羯
				case 7:
				case 8:
					try{
						System.arraycopy(aMsgofCmdPID,15,Mode,0,2);	//嚙稷嚙踝蕭Mode 
						System.arraycopy(aMsgofCmdPID,17,PID,0,2);	//嚙稷嚙踝蕭PID
						switch(aMsgofCmdPID[16]) //嚙瞑嚙稻Mode
						{
							case '1':	//Mode1
							case '5':	//Mode5
							case '6':	//Mode6
							case '9':	//Mode9
								clean_ByteArray();
								
								System.arraycopy(aMsgofCmdPID,29,Length,0,2);	//嚙稷嚙踝蕭Length
								Int_Length = ByteArray2_To_Int(Length) ;
								
								System.arraycopy(aMsgofCmdPID,38,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
								System.arraycopy(aMsgofCmdPID,40,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
								
								System.arraycopy(aMsgofCmdPID,42,Data_Packet,0, ( (Int_Length-6)*2) );		//嚙稷嚙踝蕭Data
								
								mMsg = new Message() ;
								data.putByteArray("Length", Length);
								data.putByteArray("R_Mode", R_Mode);
								data.putByteArray("R_PID", R_PID);
								data.putByteArray("Data_Packet", Data_Packet);
								mMsg.setData(data);
								
								return mMsg;
								
							case '2'://Mode2
								
								clean_ByteArray();
								
								System.arraycopy(aMsgofCmdPID,29,Length,0,2);	//嚙稷嚙踝蕭Length
								Int_Length = ByteArray2_To_Int(Length) ;
								
								System.arraycopy(aMsgofCmdPID,38,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
								System.arraycopy(aMsgofCmdPID,40,R_PID,0,2);	//嚙稷嚙踝蕭R_PID
								
								System.arraycopy(aMsgofCmdPID,44,Data_Packet,0, ( (Int_Length-7)*2) );		//嚙稷嚙踝蕭Data
								
								mMsg = new Message() ;
								data.putByteArray("Length", Length);
								data.putByteArray("R_Mode", R_Mode);
								data.putByteArray("R_PID", R_PID);
								data.putByteArray("Data_Packet", Data_Packet);
								mMsg.setData(data);
								
								return mMsg;

							case '3'://Mode3
							case '7'://Mode7

								clean_ByteArray();
								
								System.arraycopy(aMsgofCmdPID,29,Length,0,2);	//嚙稷嚙踝蕭Length
								Int_Length = ByteArray2_To_Int(Length) ;
								
								System.arraycopy(aMsgofCmdPID,38,R_Mode,0,2);	//嚙稷嚙踝蕭R_Mode
								
								System.arraycopy(aMsgofCmdPID,40,Data_Packet,0, ( (Int_Length-5)*2) );		//嚙稷嚙踝蕭Data
								
								mMsg = new Message() ;
								data.putByteArray("Length", Length);
								data.putByteArray("R_Mode", R_Mode);
								data.putByteArray("R_PID", R_PID);
								data.putByteArray("Data_Packet", Data_Packet);
								mMsg.setData(data);
								
								return mMsg;


							default:
								
								clean_ByteArray();
								
								mMsg = new Message() ;
								Bundle data = new Bundle();
								data.putByteArray("Length", Length);
								data.putByteArray("R_Mode", R_Mode);
								data.putByteArray("R_PID", R_PID);
								data.putByteArray("Data_Packet", Data_Packet);
								mMsg.setData(data);
								return mMsg;
						}
						
					}catch (Exception e){
						e.printStackTrace();
					}//end of try
					
			}
			
			
		}catch (Exception e){
			e.printStackTrace();
		}//end of trymMsg = new Message() ;
		return mMsg;
	}
 //==============================================================================================================================
	static void clean_ByteArray()
	{
		Mode = null;
		Mode = new byte[2];
		
		PID = null;
	    PID  = new byte[2];
	    
	    Check_7E8 = null;
	    Check_7E8  = new byte[3];
	    
	    Mult = null;
	    Mult  = new byte[2];
	    
	    Length = null;
	    Length  = new byte[2];
	    
	    R_Mode = null;
	    R_Mode  = new byte[2];
	    
	    R_PID = null;
	    R_PID  = new byte[2];
	    
	    RSP = null;
	    RSP  = new byte[4];
	    
	    Data_Packet = null;
		Data_Packet  = new byte[100];
	}
//==============================================================================================================================
	static int ByteArray2_To_Int(byte[] Byte2)
	{
		int data_sum,data_02,data_01;
		try
		{
			if(Byte2[0] >= 65)				
				data_02 = Byte2[0] - 55;
			else
				data_02 = Byte2[0] - 48;
			if(Byte2[1] >= 65)				
				data_01 = Byte2[1] - 55;
			else
				data_01 = Byte2[1] - 48;
			data_sum = data_02 * 16 + data_01 ;
			
			return data_sum;
					
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
	}
	static int ByteArray3_To_Int(byte[] Byte3)
	{
		int data_sum,data_03,data_02,data_01;
		try
		{
			if(Byte3[0] >= 65)				
				data_03 = Byte3[0] - 55;
			else
				data_03 = Byte3[0] - 48;
			if(Byte3[1] >= 65)				
				data_02 = Byte3[1] - 55;
			else
				data_02 = Byte3[1] - 48;
			if(Byte3[2] >= 65)				
				data_01 = Byte3[2] - 55;
			else
				data_01 = Byte3[2] - 48;
			
			data_sum = data_03 * 256 + data_02 * 16 + data_01 ;
			
			return data_sum;
					
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
	}
	static int ByteArray4_To_Int(byte[] Byte4)
	{
		int data_sum,data_04,data_03,data_02,data_01;
		try
		{
			if(Byte4[0] >= 65)				
				data_04 = Byte4[0] - 55;
			else
				data_04 = Byte4[0] - 48;
			if(Byte4[1] >= 65)				
				data_03 = Byte4[1] - 55;
			else
				data_03 = Byte4[1] - 48;
			if(Byte4[2] >= 65)				
				data_02 = Byte4[2] - 55;
			else
				data_02 = Byte4[2] - 48;
			if(Byte4[3] >= 65)				
				data_01 = Byte4[3] - 55;
			else
				data_01 = Byte4[3] - 48;
			
			data_sum = data_04 * 4096 + data_03 * 256 + data_02 * 16 + data_01 ;
			
			return data_sum;
					
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
	}
	static int Byte2_To_Int(byte mData_Packet_0, byte mData_Packet_1)
	{
		try{
			int data_sum,data_02,data_01;
			
			if(mData_Packet_0 >= 65)				
				data_02 = mData_Packet_0 - 55;
			else
				data_02 = mData_Packet_0 - 48;
			if(mData_Packet_1 >= 65)				
				data_01 = mData_Packet_1 - 55;
			else
				data_01 = mData_Packet_1 - 48;
			data_sum = data_02 * 16 + data_01 ;
				
			return data_sum;
			
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
	}
	static int ByteArray6_To_Int(byte[] Byte6)
	{
		int data_sum,data_06,data_05,data_04,data_03,data_02,data_01;
		try
		{
			if(Byte6[0] >= 65)				
				data_06 = Byte6[0] - 55;
			else
				data_06 = Byte6[0] - 48;
			if(Byte6[1] >= 65)				
				data_05 = Byte6[1] - 55;
			else
				data_05 = Byte6[1] - 48;
			if(Byte6[2] >= 65)				
				data_04 = Byte6[2] - 55;
			else
				data_04 = Byte6[2] - 48;
			if(Byte6[3] >= 65)				
				data_03 = Byte6[3] - 55;
			else
				data_03 = Byte6[3] - 48;
			if(Byte6[4] >= 65)				
				data_02 = Byte6[4] - 55;
			else
				data_02 = Byte6[4] - 48;
			if(Byte6[5] >= 65)				
				data_01 = Byte6[5] - 55;
			else
				data_01 = Byte6[5] - 48;
			
			data_sum = data_06 * 0x100000 + data_05 * 0x10000 + data_04 * 0x1000 + data_03 * 0x100 + data_02 * 0x10 + data_01 ;
			
			return data_sum;
					
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
	}
//==============================================================================================================================
	static Message Analyze_Data(String protocol_in_Mode_PID_One, byte[] mLength00, byte[] mR_Mode, byte[] mR_PID, byte[] mData_Packet, int Image_select)
	{
		byte [] aaa=protocol_in_Mode_PID_One.getBytes();
		int asd;
		String Mstring = "";
		int Set_Read_Function = 0;
		
		int mImage_select = Image_select;
//		ImageView mimage_One = image_One;
		
		
		
		
		
		switch(aaa[1])
		{
			case '1':
				asd = ByteArray4_To_Int( aaa );
				asd = asd -0x100;
				Mstring = xxx.bb1(asd);
				Set_Read_Function = 1;
				break;
			case '2':
				asd = ByteArray4_To_Int( aaa );
				asd = asd -0x200;
				Mstring = xxx.bb1(asd);
				Set_Read_Function = 1;
				break;
			case '3':
				asd = ByteArray2_To_Int( aaa );
				asd = asd -0x03;
				Mstring = xxx.bb3(asd);
				Set_Read_Function = 3;
				break;
			case '5':
				asd = ByteArray6_To_Int( aaa );
				asd = asd -0x50100;
				Mstring = xxx.bb5(asd);
				Set_Read_Function = 5;
				break;
			case '6':
				asd = ByteArray4_To_Int( aaa );
				if(Code_in_Frist <= 4)	{
					asd = asd -0x600;
					Mstring = xxx.bb6_CAN(asd);
					Set_Read_Function = 6;
				}
				if(Code_in_Frist > 4 && Code_in_Frist <= 8)	{
					asd = asd -0x601;
					Mstring = xxx.bb6_OBD(asd);
					Set_Read_Function = 6;
				}
				break;
			case '7':
				asd = ByteArray2_To_Int( aaa );
				asd = asd -0x07;
				Mstring = xxx.bb7(asd);
				Set_Read_Function = 7;
				break;
			case '9':
				asd = ByteArray4_To_Int( aaa );
				asd = asd -0x900;
				Mstring = xxx.bb9(asd);
				Set_Read_Function = 9;
				break;
				
		}
		
		
		int d=0;
		String buffer_1 = "";
		int Mode_PID = 0;
		int decide_1 = 0;
		int decide_2 = 0;
		int Image_Number_1=0;
		int Image_Number_2=0;
		String Name = "";
		
		Double 	n1 = (double) 0 ,
				n2 = (double) 0 ,
				n3 = (double) 0 ,
				/*
				n4 = (double) 0 ,
				n5 = (double) 0 ,
				n6 = (double) 0 ,
				n7 = (double) 0 ,
				n8 = (double) 0 ,
				*/
				Dashboard_Display_1 = (double) 0,
				Dashboard_Display_2 = (double) 0 ;
		
		Double 	Data_A = (double) 0 ,
				Data_B = (double) 0 ,
				Data_Sum = (double) 0 ,
				Fin_Data = (double) 0  ;
		try
		{
			byte [] byte_Mstring = Mstring.getBytes();
//			Log.v("Mstring",Mstring);
			for(int i=0;i<byte_Mstring.length;i++)
			{
				if( byte_Mstring[i] != 0x2C)//","
				{
					buffer_1 = buffer_1 + byte_Mstring[i] + " ";
					//Log.v("buffer_1",buffer_1);
				}
				if( byte_Mstring[i] == 0x2C || i == byte_Mstring.length -1 )//","
				{
					String date2=ASCII_String(buffer_1);//嚙賞成嚙緝嚙踝蕭
					//Log.v("date2",date2);
					d=d+1;
					switch(Set_Read_Function)
					{
						case 1:
							if(d ==1)	{
								Mode_PID = Integer.valueOf( date2, 16 ) ;
							}
							if(d ==2)	{
								decide_1 = Integer.valueOf( date2 ) ;
							}
							if(d ==3)	{
								decide_2 = Integer.valueOf( date2 ) ;
								switch(decide_2)
								{
									case 1:
										Dashboard_Display_2 = (double) 255;
										break;
									case 2:
										Dashboard_Display_2 = (double) 65535;
										break;
								}
							}
							if(d ==4)	{
								Image_Number_1 = Integer.valueOf( date2 ) ;
							}
							if(d ==5)	{
								Image_Number_2 = Integer.valueOf( date2 ) ;
								switch(Image_Number_2)
								{
									case 0:
										Dashboard_Display_1 = (double) 290;
										break;
									case 1:
										Dashboard_Display_1 = (double) 360;
										break;
									case 2:
										Dashboard_Display_1 = (double) 360;
										break;
									case 3:
										Dashboard_Display_1 = (double) 330;
										break;
									case 4:
										Dashboard_Display_1 = (double) 350;
										break;	
									case 5:
										Dashboard_Display_1 = (double) 360;
										break;	
									case 6:
										Dashboard_Display_1 = (double) 350;
										break;	
									case 7:
										Dashboard_Display_1 = (double) 310;
										break;
									case 8:
										Dashboard_Display_1 = (double) 290;
										break;
									case 9:
										Dashboard_Display_1 = (double) 360;
										break;
									case 10:
										Dashboard_Display_1 = (double) 360;
										break;
									case 11:
										Dashboard_Display_1 = (double) 360;
										break;
									case 12:
										Dashboard_Display_1 = (double) 310;
										break;
									case 13:
										Dashboard_Display_1 = (double) 310;
										break;
									case 14:
										Dashboard_Display_1 = (double) 330;
										break;	
									case 15:
										Dashboard_Display_1 = (double) 360;
										break;
									case 16:
										Dashboard_Display_1 = (double) 360;
										break;	
									case 17:
										Dashboard_Display_1 = (double) 330;
										break;	
									case 18:
										Dashboard_Display_1 = (double) 360;
										break;
									case 19:
										Dashboard_Display_1 = (double) 310;
										break;
									case 20:
										Dashboard_Display_1 = (double) 360;
										break;
									case 21:
										Dashboard_Display_1 = (double) 290;
										break;
									case 22:
										Dashboard_Display_1 = (double) 330;
										break;
									case 23:
										Dashboard_Display_1 = (double) 360;
										break;
									case 24:
										Dashboard_Display_1 = (double) 360;
										break;
									case 25:
										Dashboard_Display_1 = (double) 290;
										break;
									case 26:
										Dashboard_Display_1 = (double) 360;
										break;
								}
							}
							if(d ==6)	{
								Name = date2 ;
							}
							if(d ==7)	{
								n1 = Double.valueOf( date2 ) ;
							}
							if(d ==8)	{
								n2 = Double.valueOf( date2 ) ;
							}
							if(d ==9)	{
								n3 = Double.valueOf( date2 ) ;
							}
							/*
							if(d ==10)	{
								n4 = Double.valueOf( date2 ) ;
							}
							if(d ==11)	{
								n5 = Double.valueOf( date2 ) ;
							}
							if(d ==12)	{
								n6 = Double.valueOf( date2 ) ;
							}
							if(d ==13)	{
								n7 = Double.valueOf( date2 ) ;
							}
							if(d ==14)	{
								n8 = Double.valueOf( date2 ) ;
							}
							if(d ==15)	{
								Dashboard_Display_1 = Double.valueOf( date2 ) ;
							}
							if(d ==16)	{
								Dashboard_Display_2 = Double.valueOf( date2 ) ;
							}
							*/
							break;
						case 3:
						case 5:
						case 6:
						case 7:
						case 9:
							if(d ==1)	{
								Mode_PID = Integer.valueOf( date2, 16 ) ;
							}
							if(d ==2)	{
								decide_1 = Integer.valueOf( date2 ) ;
							}
							if(d ==3)	{
								decide_2 = Integer.valueOf( date2 ) ;
							}
							if(d ==4)	{
								Image_Number_1 = Integer.valueOf( date2 ) ;
							}
							if(d ==5)	{
								Image_Number_2 = Integer.valueOf( date2 ) ;
							}
							if(d ==6)	{
								Name = date2 ;
							}
							break;
							
					}
					
					buffer_1 ="";
				}
				
				
			}
			
			
			/* 讀嚙踝蕭嚙複改蕭嚙踝蕭嚙璀嚙誼查 decide_1 嚙瞑嚙稻嚙踝蕭嚙踝蕭A嚙璀嚙璀嚙瘡 decide_2 嚙諉喉蕭嚙瞑嚙稻
			 * 
			 * decide_1 = 1 嚙踝蕭 嚙踝蕭嚙�ex:( Mode1,Mode2 )
			 * decide_2 = 嚙褊包嚙踝蕭 2 Byte 嚙踝蕭 4 Byte 
			 * 
			 * 嚙踝蕭嚙踝蕭 : ( (A+n1)*n2/n3 + (B+n4)*n5/n6 )*n7/n8
			 * 
			 * 
			 * --------------------------------------------------
			 * decide_1 = 2 嚙踝蕭 嚙踝蕭嚙璀SCII嚙碼 ex:( Mode9 )
			 * decide_2 = 嚙踝蕭嚙賤成ASCII嚙碼 嚙踝蕭 嚙踝蕭嚙踝蕭嚙踝蕭X
			 * 
			 * --------------------------------------------------
			 * decide_1 = 3 嚙踝蕭 嚙碼嚙踝蕭
			 * 			
			 * 			嚙踝蕭嚙踝蕭嚙踝蕭X
			 * 
			 * --------------------------------------------------
			 */
			// 
			
			int ID_sum,ID1,ID2,ID3,ID4;
			
			String buffer_2 = "" ;
//			Log.e("decide_1", String.valueOf(decide_1));
//			Log.e("decide_2", String.valueOf(decide_2));
//			
//			Log.e("mData_Packet[0]", String.valueOf(mData_Packet[0]));
//			Log.e("mData_Packet[1]", String.valueOf(mData_Packet[1]));
//			Log.e("mData_Packet[2]", String.valueOf(mData_Packet[2]));
//			Log.e("mData_Packet[3]", String.valueOf(mData_Packet[3]));
//			
//			Log.e("Dashboard_Display_1", String.valueOf(Dashboard_Display_1));
//			Log.e("Dashboard_Display_2", String.valueOf(Dashboard_Display_2));
			switch(decide_1)
			{
				
				case 1:	//decide_1 = 1 嚙踝蕭 嚙踝蕭嚙�
					//Data_A = 0;
					//Data_B = 0;
					if( decide_2 == 1 )
					{
						Data_A = (double) ByteArray2_To_Int(mData_Packet);
						Data_Sum = Data_A;
					}
					if( decide_2 == 2 )
					{
						Data_A = (double) Byte2_To_Int(mData_Packet[0],mData_Packet[1]);
						Data_B = (double) Byte2_To_Int(mData_Packet[2],mData_Packet[3]);
						Data_Sum = Data_A *256 + Data_B;
					}
					//Fin_Data = ((Data_A + n1)*n2/n3 + (Data_B+n4)*n5/n6)*n7/n8 ;
					
					if( mImage_select == 1 )
					{
						//Fin_Data = ((Data_A + n1)*n2/n3 + (Data_B+n4)*n5/n6)*n7/n8 ;
						Fin_Data = ( Data_Sum + n1 ) * n2 / n3 ;
					}
					if( mImage_select == 2 )
					{
						Fin_Data = Data_Sum * Dashboard_Display_1 / Dashboard_Display_2;
					}
					if( mImage_select == 3 )
					{
						Fin_Data = Data_Sum * 80 / Dashboard_Display_2;
					}
					
					break;
				case 2:	//decide_1 = 2 嚙踝蕭 嚙踝蕭嚙璀SCII嚙碼
					if(decide_2 == 1)
					{
						for(int i = 0;i<mData_Packet.length;i=i+2)
						{
							if(mData_Packet[i] >= 65)				
								ID1 = mData_Packet[i] - 55;
							else
								ID1 = mData_Packet[i] - 48;
							if(mData_Packet[i+1] >= 65)				
								ID2 = mData_Packet[i+1] - 55;
							else
								ID2 = mData_Packet[i+1] - 48;
							ID_sum = ID1 * 16 + ID2 ;
							buffer_2 = buffer_2 + String.valueOf(ID_sum) + " ";
							
							if(mData_Packet[i] == 0x00)
							{
								buffer_2 = ASCII_String(buffer_2);
								break;
							}
							
						}
					}
					if(decide_2 == 2)
					{
						buffer_2 = new String( mData_Packet );
					}
					break;
				case 3:	//decide_1 = 3 嚙踝蕭 嚙碼嚙踝蕭
					
					buffer_2 = new String( mData_Packet );
					
					break;
				case 4://嚙踝蕭~嚙碼
					
					for(int i = 0 ;i<mData_Packet.length;i=i+4)
					{
						if(mData_Packet[i] == 0x00)
						{
							break;
						}
						if(mData_Packet[i] >= 65)				
							ID1 = mData_Packet[i] - 55;
						else
							ID1 = mData_Packet[i] - 48;
						
						if(mData_Packet[i+1] >= 65)				
							ID2 = mData_Packet[i+1] - 55;
						else
							ID2 = mData_Packet[i+1] - 48;
						
						if(mData_Packet[i+2] >= 65)				
							ID3 = mData_Packet[i+2] - 55;
						else
							ID3 = mData_Packet[i+2] - 48;
						
						if(mData_Packet[i+3] >= 65)				
							ID4 = mData_Packet[i+3] - 55;
						else
							ID4 = mData_Packet[i+3] - 48;
						
						switch(ID1)
						{
							case 0:
								buffer_2 = buffer_2 + "P0" ;
								break;
							case 1:
								buffer_2 = buffer_2 + "P1" ;
								break;
							case 2:
								buffer_2 = buffer_2 + "P2" ;
								break;
							case 3:
								buffer_2 = buffer_2 + "P3" ;
								break;
								
							case 4:
								buffer_2 = buffer_2 + "C0" ;
								break;
							case 5:
								buffer_2 = buffer_2 + "C1" ;
								break;
							case 6:
								buffer_2 = buffer_2 + "C2" ;
								break;
							case 7:
								buffer_2 = buffer_2 + "C3" ;
								break;
								
							case 8:
								buffer_2 = buffer_2 + "B0" ;
								break;
							case 9:
								buffer_2 = buffer_2 + "B1" ;
								break;
							case 10:
								buffer_2 = buffer_2 + "B2" ;
								break;
							case 11:
								buffer_2 = buffer_2 + "B3" ;
								break;
								
							case 12:
								buffer_2 = buffer_2 + "U0" ;
								break;
							case 13:
								buffer_2 = buffer_2 + "U1" ;
								break;
							case 14:
								buffer_2 = buffer_2 + "U2" ;
								break;
							case 15:
								buffer_2 = buffer_2 + "U3" ;
								break;
								
						}
						
						ID_sum = ID2 * 256 +ID3 * 16 + ID4 ;
						buffer_2 = buffer_2 + String.format("%03X",ID_sum) + " ";
						
					}
					
					break;
					
					
			}
//-------------------------------------------------------------------------------------------------------------------------
//			switch(mImage_select)//嚙瞑嚙稻嚙瞌嚙誕形或數字
//			{
//				case 1://嚙複字
//					
//					switch( Image_Number_1 )
//					{
//						case 0:
//							mimage_One.setImageResource(R.drawable.b0);
//							break;
//						case 1:
//							mimage_One.setImageResource(R.drawable.b1);
//							break;
//						case 2:
//							mimage_One.setImageResource(R.drawable.b2);
//							break;
//						case 3:
//							mimage_One.setImageResource(R.drawable.b3);
//							break;
//						case 4:
//							mimage_One.setImageResource(R.drawable.b4);
//							break;
//						case 5:
//							mimage_One.setImageResource(R.drawable.b5);
//							break;
//						case 6:
//							mimage_One.setImageResource(R.drawable.b6);
//							break;
//						case 7:
//							mimage_One.setImageResource(R.drawable.b7);
//							break;
//						case 8:
//							mimage_One.setImageResource(R.drawable.b8);
//							break;
//						case 9:
//							mimage_One.setImageResource(R.drawable.b9);
//							break;
//						case 10:
//							mimage_One.setImageResource(R.drawable.b10);
//							break;
//						case 11:
//							mimage_One.setImageResource(R.drawable.b11);
//							break;
//						case 12:
//							mimage_One.setImageResource(R.drawable.b12);
//							break;
//						case 13:
//							mimage_One.setImageResource(R.drawable.b13);
//							break;
//						case 14:
//							mimage_One.setImageResource(R.drawable.b14);
//							break;
//						case 15:
//							mimage_One.setImageResource(R.drawable.b15);
//							break;
//						case 16:
//							mimage_One.setImageResource(R.drawable.b16);
//							break;
//						case 17:
//							mimage_One.setImageResource(R.drawable.b17);
//							break;
//						case 18:
//							mimage_One.setImageResource(R.drawable.b18);
//							break;
//						case 19:
//							mimage_One.setImageResource(R.drawable.b19);
//							break;
//					}
//					break;
//				case 2://嚙誕改蕭
//					switch( Image_Number_2 )
//					{
//						case 0:
//							mimage_One.setImageResource(R.drawable.a1);
//							break;
//						case 1:
//							mimage_One.setImageResource(R.drawable.a1);
//							break;
//						case 2:
//							mimage_One.setImageResource(R.drawable.a2);
//							break;
//						case 3:
//							mimage_One.setImageResource(R.drawable.a3);
//							break;
//						case 4:
//							mimage_One.setImageResource(R.drawable.a4);
//							break;
//						case 5:
//							mimage_One.setImageResource(R.drawable.a5);
//							break;
//						case 6:
//							mimage_One.setImageResource(R.drawable.a6);
//							break;
//						case 7:
//							mimage_One.setImageResource(R.drawable.a7);
//							break;
//						case 8:
//							mimage_One.setImageResource(R.drawable.a8);
//							break;
//						case 9:
//							mimage_One.setImageResource(R.drawable.a9);
//							break;
//						case 10:
//							mimage_One.setImageResource(R.drawable.a10);
//							break;
//						case 11:
//							mimage_One.setImageResource(R.drawable.a11);
//							break;
//						case 12:
//							mimage_One.setImageResource(R.drawable.a12);
//							break;
//						case 13:
//							mimage_One.setImageResource(R.drawable.a13);
//							break;
//						case 14:
//							mimage_One.setImageResource(R.drawable.a14);
//							break;
//						case 15:
//							mimage_One.setImageResource(R.drawable.a15);
//							break;
//						case 16:
//							mimage_One.setImageResource(R.drawable.a16);
//							break;
//						case 17:
//							mimage_One.setImageResource(R.drawable.a17);
//							break;
//						case 18:
//							mimage_One.setImageResource(R.drawable.a18);
//							break;
//						case 19:
//							mimage_One.setImageResource(R.drawable.a19);
//							break;
//						case 20:
//							mimage_One.setImageResource(R.drawable.a20);
//							break;
//						case 21:
//							mimage_One.setImageResource(R.drawable.a21);
//							break;
//						case 22:
//							mimage_One.setImageResource(R.drawable.a22);
//							break;
//						case 23:
//							mimage_One.setImageResource(R.drawable.a23);
//							break;
//						case 24:
//							mimage_One.setImageResource(R.drawable.a24);
//							break;
//						case 25:
//							mimage_One.setImageResource(R.drawable.a25);
//							break;
//					}
//					break;
//			}
			
			mmMsg = new Message() ;
			mmdata = new Bundle();
			mmdata.putInt("Mode_PID", Mode_PID);
			mmdata.putInt("decide_1", decide_1);
			mmdata.putString("Name", Name);
			
			mmdata.putString("buffer_2", buffer_2);
			
			mmdata.putDouble("Fin_Data", Fin_Data);
			
			mmMsg.setData(mmdata);
			
			return mmMsg;
			
		}
		catch (Exception e){
			e.printStackTrace();
		}//end of try
		
		
		
		
		mmMsg = new Message() ;
		mmdata = new Bundle();
		mmdata.putString("Name", "");
		mmdata.putInt("n1", 0);
		mmdata.putInt("n2", 0);
		mmdata.putInt("n3", 0);
		mmMsg.setData(mmdata);
		return mmMsg;
		
		
		/*
		int data = 0;
		int asd = 0;
		byte [] aaa=protocol_in_Mode_PID_One.getBytes();
		asd = Byte4_To_Int( aaa );
		try
		{
			switch( asd )
			{
				case 0x0105:
					data = Byte2_To_Int(mData_Packet);
					data = data -40 ;
					return data;
				case 0x010C:
					data = Byte4_To_Int(mData_Packet);
					data = data / 4 ;
					return data;
			}
		}catch (Exception e){
			e.printStackTrace();
		}//end of try
		return -1;
		*/
		
		//byte[] zzz = new byte[4] ;
		//zzz ={'0','1','2','3'};
		/*byte[] zzz ={'0','0','0','0'};
		try
		{
			System.arraycopy(mR_Mode,0,zzz,0, 2 );
			System.arraycopy(mR_PID,0,zzz,2, 2 );
		}catch (Exception e){
			e.printStackTrace();
		}//end of try*/
		//return Integer.parseInt( protocol_in_Mode_PID_One );
		//return zzz;
		
		
	}
	
//==============================================================================================================================
	public static String ASCII_String(String s){    	
   	 String ss="";
   	 String[]chars=s.split(" ");
   	 for(int i=0;i<chars.length;i++){ 
   		 ss=ss+(char)Integer.parseInt(chars[i])+"";
   	 }
   	 return ss;
    }
//==============================================================================================================================
	
	
}

class xxx {
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb1(int i)
		{
			String [] Inventory = {
					/*
					 * Mode_PID,decide_1,decide_2,Image_Number_1,Image_Number_2,Name,n1,n2,n3;
					 * 
					 * Mode_PID = Mode + PID
					 * 
					 * decide_1 = 嚙誕用佗蕭堣嚙緹嚙踝蕭 
					 * 				1 嚙踝蕭 嚙踝蕭嚙?
					 * 				2 嚙踝蕭 ASCII
					 * 				3 嚙踝蕭 嚙碼嚙踝蕭
					 * 
					 * decide_2 = 嚙踝蕭峖h嚙踝蕭Byte
					 * 				1 嚙踝蕭 嚙箴嚙踝蕭 1 Byte
					 *  			2 嚙踝蕭 嚙箴嚙踝蕭 2 Byte
					 * 
					 * Name = 嚙磕嚙踝蕭
					 * 
					 * 嚙踝蕭嚙踝蕭 = ( Data +n1 )*n2/n3
					 * 
					 */
					"0100,3,0,0,0,PIDs supported [01 - 20],0,1,1",
					"0101,3,4,0,0,Monitor status since DTCs cleared,0,1,1",
					"0102,2,2,0,23,DTC that caused required freeze frame data storage,0,1,1",
					"0103,3,2,0,0,Fuel system 1 status,0,1,1",
					
					"0104,1,1,1,13,Calculated LOAD Value,0,100,255",
					"0105,1,1,3,8,Engine coolant temperature,-40,1,1",
					
					"0106,1,1,1,6,Short Term Fuel Trim - Bank 1,-128,100,128",
					"0107,1,1,1,6,Long Term Fuel Trim 嚙碾 Bank 1,-128,100,128",
					"0108,1,1,1,6,Short Term Fuel Trim - Bank 2,-128,100,128",
					"0109,1,1,1,6,Long Term Fuel Trim 嚙碾 Bank 2,-128,100,128",
					
					"010A,1,1,7,25,Fuel Rail Pressure (gauge),0,3,1",
					"010B,1,1,6,14,Intake Manifold Absolute Pressure,1,1,1",
					
					"010C,1,2,15,21,Engine RPM,0,1,4",
					"010D,1,1,9,14,Vehicle Speed Sensor,0,1,1",
					
					"010E,1,1,14,7,Ignition Timing Advance for #1 Cylinder,-128,1,2",
					"010F,1,1,3,8,Intake Air Temperature,-40,1,1",
					"0110,1,2,5,16,MAF air flow rate,0,1,100",
					"0111,1,1,1,13,Absolute Throttle Position,0,100,255",
					"0112,3,1,0,0,Commanded Secondary Air Status,0,1,1",
					"0113,2,1,0,0,Location of Oxygen Sensors,0,1,1",
					
					"0114,1,1,17,9,Bank 1 嚙碾 Sensor 1,0,1,200",
					"0115,1,1,17,9,Bank 1 嚙碾 Sensor 2,0,1,200",
					"0116,1,1,17,9,Bank 1 嚙碾 Sensor 3,0,1,200",
					"0117,1,1,17,9,Bank 1 嚙碾 Sensor 4,0,1,200",
					"0118,1,1,17,9,Bank 2 嚙碾 Sensor 1,0,1,200",
					"0119,1,1,17,9,Bank 2 嚙碾 Sensor 2,0,1,200",
					"011A,1,1,17,9,Bank 2 嚙碾 Sensor 3,0,1,200",
					"011B,1,1,17,9,Bank 2 嚙碾 Sensor 4,0,1,200",
					"011C,2,1,0,0,OBD requirements to which vehicle is designed,0,1,1",
					"011D,2,1,0,0,Location of oxygen sensors,0,1,1",
					"011E,3,1,0,0,Auxiliary Input Status,0,1,1",
					"011F,1,2,16,23,Time Since Engine Start,0,1,1",
					
					
					"0120,3,0,0,0,PIDs supported [21 - 40],0,1,1",
					"0121,1,2,9,23,Distance Travelled While MIL is Activated,0,1,1",
					"0122,1,2,8,20,Fuel Rail Pressure relative to manifold vacuum,0,5178,65535",
					"0123,1,2,7,24,Fuel Rail Pressure,0,10,1",
					"0124,1,2,0,26,Bank 1 嚙碾 Sensor 1 (wide range O2S),0,2,65535",
					"0125,1,2,0,26,Bank 2 嚙碾 Sensor 2 (wide range O2S),0,2,65535",
					"0126,1,2,0,26,Bank 3 嚙碾 Sensor 3 (wide range O2S),0,2,65535",
					"0127,1,2,0,26,Bank 4 嚙碾 Sensor 4 (wide range O2S),0,2,65535",
					"0128,1,2,0,26,Bank 1 嚙碾 Sensor 1 (wide range O2S),0,2,65535",
					"0129,1,2,0,26,Bank 2 嚙碾 Sensor 2 (wide range O2S),0,2,65535",
					"012A,1,2,0,26,Bank 3 嚙碾 Sensor 3 (wide range O2S),0,2,65535",
					"012B,1,2,0,26,Bank 4 嚙碾 Sensor 4 (wide range O2S),0,2,65535",
					"012C,1,1,1,13,Commanded EGR,0,100,255",
					"012D,1,1,1,5,EGR Error,-128,100,128",
					"012E,1,1,1,13,Commanded Evaporative Purge,0,100,255",
					"012F,1,1,1,13,Fuel Level Input,0,100,255",
					"0130,1,1,0,14,Number of warm-ups since diagnostic trouble codes cleared,0,1,1",
					"0131,1,2,9,23,Distance since diagnostic trouble codes cleared,0,1,1",
					"0132,1,2,20,2,Evap System Vapour Pressure,-32768,1,4",
					"0133,1,1,6,14,Barometric Pressure,0,1,1",
					"0134,1,2,0,26,Bank 1 嚙碾 Sensor 1 (wide range O2S),0,2,65535",
					"0135,1,2,0,26,Bank 1 嚙碾 Sensor 2 (wide range O2S),0,2,65535",
					"0136,1,2,0,26,Bank 1 嚙碾 Sensor 3 (wide range O2S),0,2,65535",
					"0137,1,2,0,26,Bank 1 嚙碾 Sensor 4 (wide range O2S),0,2,65535",
					"0138,1,2,0,26,Bank 2 嚙碾 Sensor 1 (wide range O2S),0,2,65535",
					"0139,1,2,0,26,Bank 2 嚙碾 Sensor 2 (wide range O2S),0,2,65535",
					"013A,1,2,0,26,Bank 2 嚙碾 Sensor 3 (wide range O2S),0,2,65535",
					"013B,1,2,0,26,Bank 2 嚙碾 Sensor 4 (wide range O2S),0,2,65535",
					"013C,1,2,3,20,Catalyst Temperature Bank 1,Sensor 1,-40,1,10",
					"013D,1,2,3,20,Catalyst Temperature Bank 2,Sensor 1,-40,1,10",
					"013E,1,2,3,20,Catalyst Temperature Bank 1,Sensor 2,-40,1,10",
					"013F,1,2,3,20,Catalyst Temperature Bank 2,Sensor 2.-40,1,10",
					
					"0140,3,0,0,0,PIDs supported [41 - 60],0,1,1",
					"0141,3,4,0,0,Monitor status this drive cycle,0,1,1",
					"0142,1,2,18,11,Control module voltage,0,1,1000",
					"0143,1,2,1,22,Absolute Load Value,0,100,255",
					"0144,1,2,0,26,Command Equivalence Ratio,0,2,65535",
					"0145,1,1,1,13,Relative Throttle Position,0,100,255",
					"0146,1,1,3,8,Ambient air temperature(same scaling as IAT - $0F),-40,1,1",
					"0147,1,1,1,13,Absolute Throttle Position B,0,100,255",
					"0148,1,1,1,13,Absolute Throttle Position C,0,100,255",
					"0149,1,1,1,13,Accelerator Pedal Position D,0,100,255",
					"014A,1,1,1,13,Accelerator Pedal Position E,0,100,255",
					"014B,1,1,1,13,Accelerator Pedal Position F,0,100,255",
					"014C,1,1,1,13,Commanded Throttle Actuator Control,0,100,255",
					"014D,1,2,12,23,Time run by the engine while MIL is activated,0,1,1",
					"014E,1,2,12,23,Time since diagnostic trouble codes cleared,0,1,1",
					"014F,1,1,19,14,Maximum value for Equivalence Ratio,0,1,1",
					
					"0150,1,1,4,17,Maximum value for Air Flow Rate from Mass Air Flow Sensor,0,10,1",
					"0151,2,1,0,0,Type of fuel currently being utilized by the vehicle,0,1,1",
					"0152,1,1,1,13,Alcohol Fuel Percentage,0,100,255",
					"0153,1,2,8,18,Absolute Evap System Vapour Pressure,0,1,200",
					"0154,1,2,20,1,Evap System Vapour Pressure,-32767,1,1",
					"0155,1,1,1,6,Short Term Secondary O2 Sensor Fuel Trim 嚙碾 Bank 1(use if only 1 fuel trim value),-128,100,128",
					"0156,1,2,1,6,Long Term Secondary O2 Sensor Fuel Trim 嚙碾 Bank 1(use if only 1 fuel trim value),-128,100,128",
					"0157,1,2,1,6,Short Term Secondary O2 Sensor Fuel Trim - Bank 2(use if only 1 fuel trim value),-128,100,128",
					"0158,1,2,1,6,Long Term Secondary O2 Sensor Fuel Trim 嚙碾 Bank 2(use if only 1 fuel trim value),-128,100,128",
					"0159,1,2,8,24,Fuel Rail Pressure(absolute),0,10,1",
					"015A,1,1,1,13,Relative Accelerator Pedal Position,0,100,255"
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb9(int i)
		{
			String [] Inventory = {
					"0900,2,2,0,1",
					"0901,2,2,0,1",
					"0902,2,1,0,1",
					"0903,2,2,0,1",
					"0904,2,1,0,1,Calibration ID message count for PID ( Only for ISO 9141-2, ISO 14230-4 and SAE J1850 )",
					"0905,2,2,0,1,Calibration ID,-40,1,1,0,1,1,1,1",
					"0906,2,2,0,1,Calibration Verification Numbers (CVN)",
					"0907,2,2,0,1",
					"0908,2,2,0,1",
					"0909,2,2,0,1",
					"090A,2,1,0,1",
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb3(int i)
		{
			String [] Inventory = {
					"0300,4,1,0,1,Fault_Data",
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb5(int i)
		{
			String [] Inventory = {
					"050100,3,2,0,1,OBD Monitor IDs supported ($01 嚙碾 $20)",
					
					"050101,3,2,0,1,O2 Sensor Monitor Bank 1 Sensor 1",
					"050102,3,2,0,1,O2 Sensor Monitor Bank 1 Sensor 2",
					"050103,3,2,0,1,O2 Sensor Monitor Bank 1 Sensor 3",
					"050104,3,2,0,1,O2 Sensor Monitor Bank 1 Sensor 4",
					
					"050105,3,2,0,1,O2 Sensor Monitor Bank 2 Sensor 1",
					"050106,3,2,0,1,O2 Sensor Monitor Bank 2 Sensor 2",
					"050107,3,2,0,1,O2 Sensor Monitor Bank 2 Sensor 3",
					"050108,3,2,0,1,O2 Sensor Monitor Bank 2 Sensor 4",
					
					"050109,3,2,0,1,O2 Sensor Monitor Bank 3 Sensor 1",
					"05010A,3,2,0,1,O2 Sensor Monitor Bank 3 Sensor 2",
					"05010B,3,2,0,1,O2 Sensor Monitor Bank 3 Sensor 3",
					"05010C,3,2,0,1,O2 Sensor Monitor Bank 3 Sensor 4",
					
					"05010D,2,2,0,1,O2 Sensor Monitor Bank 4 Sensor 1",
					"05010E,2,2,0,1,O2 Sensor Monitor Bank 4 Sensor 2",
					"05010F,2,2,0,1,O2 Sensor Monitor Bank 4 Sensor 3",
					"050110,2,1,0,1,O2 Sensor Monitor Bank 4 Sensor 4",
					
					
					
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb6_CAN(int i)
		{
			String [] Inventory = {
					"0600,3,1,0,1,OBD Monitor IDs supported ($01 - $20)",
					"0601,3,1,0,1,Oxygen Sensor Monitor Bank 1 嚙碾 Sensor 1",
					"0602,3,1,0,1,Oxygen Sensor Monitor Bank 1 嚙碾 Sensor 2",
					"0603,3,1,0,1,Oxygen Sensor Monitor Bank 1 嚙碾 Sensor 3",
					"0604,3,1,0,1,Oxygen Sensor Monitor Bank 1 嚙碾 Sensor 4",
					"0605,3,1,0,1,Oxygen Sensor Monitor Bank 2 嚙碾 Sensor 1",
					"0606,3,1,0,1,Oxygen Sensor Monitor Bank 2 嚙碾 Sensor 2",
					"0607,3,1,0,1,Oxygen Sensor Monitor Bank 2 嚙碾 Sensor 3",
					"0608,3,1,0,1,Oxygen Sensor Monitor Bank 2 嚙碾 Sensor 4",
					"0609,3,1,0,1,Oxygen Sensor Monitor Bank 3 嚙碾 Sensor 1",
					"060A,3,1,0,1,Oxygen Sensor Monitor Bank 3 嚙碾 Sensor 2",
					"060B,3,1,0,1,Oxygen Sensor Monitor Bank 3 嚙碾 Sensor 3",
					"060C,3,1,0,1,Oxygen Sensor Monitor Bank 4 嚙碾 Sensor 4",
					"060D,3,1,0,1,Oxygen Sensor Monitor Bank 4 嚙碾 Sensor 1",
					"060E,3,1,0,1,Oxygen Sensor Monitor Bank 4 嚙碾 Sensor 2",
					"060F,3,1,0,1,Oxygen Sensor Monitor Bank 4 嚙碾 Sensor 3",
					"0610,3,1,0,1,Oxygen Sensor Monitor Bank 4 嚙碾 Sensor 4",
					"0611,3,1,0,1,ISO/SAE reserved",
					"0612,3,1,0,1,ISO/SAE reserved",
					"0613,3,1,0,1,ISO/SAE reserved",
					"0614,3,1,0,1,ISO/SAE reserved",
					"0615,3,1,0,1,ISO/SAE reserved",
					"0616,3,1,0,1,ISO/SAE reserved",
					"0617,3,1,0,1,ISO/SAE reserved",
					"0618,3,1,0,1,ISO/SAE reserved",
					"0619,3,1,0,1,ISO/SAE reserved",
					"061A,3,1,0,1,ISO/SAE reserved",
					"061B,3,1,0,1,ISO/SAE reserved",
					"061C,3,1,0,1,ISO/SAE reserved",
					"061D,3,1,0,1,ISO/SAE reserved",
					"061E,3,1,0,1,ISO/SAE reserved",
					"061F,3,1,0,1,ISO/SAE reserved",
					"061F,3,1,0,1,ISO/SAE reserved",
					
					"0620,3,1,0,1,OBD Monitor IDs supported ($21 嚙碾 $40)",
					"0621,3,1,0,1,Catalyst Monitor Bank 1",
					"0622,3,1,0,1,Catalyst Monitor Bank 2",
					"0623,3,1,0,1,Catalyst Monitor Bank 3",
					"0624,3,1,0,1,Catalyst Monitor Bank 4",
					"0625,3,1,0,1,ISO/SAE reserved",
					"0626,3,1,0,1,ISO/SAE reserved",
					"0627,3,1,0,1,ISO/SAE reserved",
					"0628,3,1,0,1,ISO/SAE reserved",
					"0629,3,1,0,1,ISO/SAE reserved",
					"062A,3,1,0,1,ISO/SAE reserved",
					"062B,3,1,0,1,ISO/SAE reserved",
					"062C,3,1,0,1,ISO/SAE reserved",
					"062D,3,1,0,1,ISO/SAE reserved",
					"062E,3,1,0,1,ISO/SAE reserved",
					"062F,3,1,0,1,ISO/SAE reserved",
					"0630,3,1,0,1,ISO/SAE reserved",
					"0631,3,1,0,1,EGR Monitor Bank 1",
					"0632,3,1,0,1,EGR Monitor Bank 2",
					"0633,3,1,0,1,EGR Monitor Bank 3",
					"0634,3,1,0,1,EGR Monitor Bank 4",
					"0635,3,1,0,1,ISO/SAE reserved",
					"0636,3,1,0,1,ISO/SAE reserved",
					"0637,3,1,0,1,ISO/SAE reserved",
					"0638,3,1,0,1,ISO/SAE reserved",
					"0639,3,1,0,1,EVAP Monitor (Cap Off)",
					"063A,3,1,0,1,EVAP Monitor (0,090嚙踝蕭)",
					"063B,3,1,0,1,EVAP Monitor (0,040嚙踝蕭)",
					"063C,3,1,0,1,EVAP Monitor (0,020嚙踝蕭)",
					"063D,3,1,0,1,Purge Flow Monitor",
					"063E,3,1,0,1,ISO/SAE reserved",
					"063F,3,1,0,1,ISO/SAE reserved",
					
					"0640,3,1,0,1,OBD Monitor IDs supported ($41 嚙碾 $60)",
					"0641,3,1,0,1,Oxygen Sensor Heater Monitor Bank 1 嚙碾 Sensor 1",
					"0642,3,1,0,1,Oxygen Sensor Heater Monitor Bank 1 嚙碾 Sensor 2",
					"0643,3,1,0,1,Oxygen Sensor Heater Monitor Bank 1 嚙碾 Sensor 3",
					"0644,3,1,0,1,Oxygen Sensor Heater Monitor Bank 1 嚙碾 Sensor 4",
					"0645,3,1,0,1,Oxygen Sensor Heater Monitor Bank 2 嚙碾 Sensor 1",
					"0646,3,1,0,1,Oxygen Sensor Heater Monitor Bank 2 嚙碾 Sensor 2",
					"0647,3,1,0,1,Oxygen Sensor Heater Monitor Bank 2 嚙碾 Sensor 3",
					"0648,3,1,0,1,Oxygen Sensor Heater Monitor Bank 2 嚙碾 Sensor 4",
					"0649,3,1,0,1,Oxygen Sensor Heater Monitor Bank 3 嚙碾 Sensor 1",
					"064A,3,1,0,1,Oxygen Sensor Heater Monitor Bank 3 嚙碾 Sensor 2",
					"064B,3,1,0,1,Oxygen Sensor Heater Monitor Bank 3 嚙碾 Sensor 3",
					"064C,3,1,0,1,Oxygen Sensor Heater Monitor Bank 3 嚙碾 Sensor 4",
					"064D,3,1,0,1,Oxygen Sensor Heater Monitor Bank 4 嚙碾 Sensor 1",
					"064E,3,1,0,1,Oxygen Sensor Heater Monitor Bank 4 嚙碾 Sensor 2",
					"064F,3,1,0,1,Oxygen Sensor Heater Monitor Bank 4 嚙碾 Sensor 3",
					"0650,3,1,0,1,Oxygen Sensor Heater Monitor Bank 4 嚙碾 Sensor 4",
					"0651,3,1,0,1,ISO/SAE reserved",
					"0652,3,1,0,1,ISO/SAE reserved",
					"0653,3,1,0,1,ISO/SAE reserved",
					"0654,3,1,0,1,ISO/SAE reserved",
					"0655,3,1,0,1,ISO/SAE reserved",
					"0656,3,1,0,1,ISO/SAE reserved",
					"0657,3,1,0,1,ISO/SAE reserved",
					"0658,3,1,0,1,ISO/SAE reserved",
					"0659,3,1,0,1,ISO/SAE reserved",
					"065A,3,1,0,1,ISO/SAE reserved",
					"065B,3,1,0,1,ISO/SAE reserved",
					"065C,3,1,0,1,ISO/SAE reserved",
					"065D,3,1,0,1,Purge Flow Monitor",
					"065E,3,1,0,1,ISO/SAE reserved",
					"065F,3,1,0,1,ISO/SAE reserved",
					
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
		static String bb6_OBD(int i)
		{
			String [] Inventory = {
					"0601,3,1,0,1,Rich to lean sensor threshold voltage (constant)",
					"0602,3,1,0,1,Lean to rich sensor threshold voltage (constant)",
					"0603,3,1,0,1,Low sensor voltage for switch time calculation (constant)",
					"0604,3,1,0,1,High sensor voltage for switch time calculation (constant)",
					"0605,3,1,0,1,Rich to lean sensor switch time (calculated)",
					
			};
			return Inventory[i];
		}
	//------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------------
			static String bb7(int i)
			{
				String [] Inventory = {
					"0700,3,1,0,1,Fault_Data",
						
				};
				return Inventory[i];
			}
	}