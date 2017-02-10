package study.BluetoothGattoOBD;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.os.Message;
import android.util.Log;

public class Lib_obd {

	Protocol pdd = null;

	char enter = 0x0D, Wrap = 0x0A;
	String[] read_Message = new String[512];
	int s = 0;
	static String Final_readMessage = "0";
	static int check_ok = 0;
	String Protocol_in_Frist = "A";

	StringBuffer sMsgofCmdPID = new StringBuffer(128);
	String Protocol_in_Mode_PID_One = "0105";
	Message mMsg;
	byte[] mLength = null;
	byte[] mR_Mode = null;
	byte[] mR_PID = null;
	byte[] mData_Packet = null;
	byte[] PID = new byte[2];
	byte[] check = new byte[2];
	String Length;
	String R_Mode;
	String R_PID;
	String Data_Packet;

	Message mmMsg;

	int total1 = 0;

	public void readMessage(String readMessage) {
		String First_readMessage = "0";
		readMessage = readMessage.replace("\r\n", "");
		read_Message[s] = readMessage;
		s++;
		// ////////////////////////////////////////////////
		if (readMessage.equals("OK")) {
			for (int i = 0; i < s; i++) {
				if (i == 0) {
					// String CK_First_readMessage = "";
					// First_readMessage = read_Message [i];
					// CK_First_readMessage = First_readMessage.substring(0,1);
					// if(!CK_First_readMessage.equals("O")){
					// First_readMessage = "";
					// }
					First_readMessage = read_Message[i];
					if (First_readMessage.length() < 5) {
						First_readMessage = "";
					}
				} else {
					First_readMessage = First_readMessage + read_Message[i];
				}
			}
			Final_readMessage = First_readMessage;
			check_ok = 1;
			s = 0;
		}
	}

	public String Get_HW_Version() {
		Final_readMessage = "0";
		check_ok = 0;
		// String HW_Version = "v"+ enter;
		String HW_Version = "v" + enter;
		String BT_v_return = "0";
		int begiconnect_v = 0;
		int again = 0;

		try {
			pdd.ProtocolTEST(HW_Version);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (begiconnect_v == 0) {
			BT_v_return = Final_readMessage;
			if (check_ok > 0) {
				Log.v("123", "123");
				if (begiconnect_v == 0 && BT_v_return.length() >= 13) {
					if (BT_v_return.substring(0, 13).equals("OBD Scan IM V")) {
						begiconnect_v = 1;
						BT_v_return = BT_v_return.substring(0,
								BT_v_return.lastIndexOf("OK"));
						Log.v("Finish v", BT_v_return);
					}

				} else if (begiconnect_v == 2 && BT_v_return.length() >= 13) {
					if (BT_v_return.substring(0, 13).equals("OBD Scan IM V")) {
						begiconnect_v = 0;
						Log.v("Finish v - before STOP", BT_v_return);
					}

				}
				// ERROR
				else if (BT_v_return.length() == 5) {
					if (BT_v_return.substring(0, 5).equals("ERROR")) {
						begiconnect_v = 2;
						Log.v("ERROR", BT_v_return.substring(0, 5));
					}
				}
				// STOP
				else if (BT_v_return.length() == 13) {
					if (BT_v_return.substring(0, 13).equals("OBD SCAN STOP")) {
						begiconnect_v = 2;
						Log.v("STOP",
								BT_v_return.substring(0, BT_v_return.length()));
					}
				}

			} else if (check_ok == 0) {
				again++;
				if (again > 300000000) {
					again = 0;
					begiconnect_v = 2;
					BT_v_return = "No return value";
				}
			}
		}

		// Thread t = new Thread(new UploadRunnable(HW_Version));
		// t.start();

		return BT_v_return;
	}

	// class UploadRunnable implements Runnable
	//
	// {
	// String strTxt = null;
	//
	// // �غc�l�A�]�w�n�Ǫ��r��
	// public UploadRunnable(String strTxt)
	// {
	// this.strTxt = strTxt;
	// }
	// @Override
	// public void run()
	// {
	//
	// }
	// }

	public String Get_CAR_Protocol() {
		Protocol_in_Frist = "A";
		Final_readMessage = "0";
		check_ok = 0;
		// String CAR_Protocol = "z"+ enter;
		String CAR_Protocol = "z" + enter;
		String BT_z_return = "0";
		int begiconnect_z = 0;
		int again = 0;

		try {
			pdd.ProtocolTEST(CAR_Protocol);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (begiconnect_z == 0) {

			BT_z_return = Final_readMessage;
			if (check_ok > 0) {
				if (begiconnect_z == 0 && BT_z_return.length() >= 14) {
					if (BT_z_return.substring(0, 14).equals("CAN 11bits 250")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "A";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 29bits 250")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "B";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 11bits 500")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "C";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 29bits 500")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "D";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD ISO9141   ")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "E";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD ISO14230  ")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "F";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD J1850 PWM ")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "G";
						begiconnect_z = 1;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD J1850 VPW ")) {
						BT_z_return = BT_z_return.substring(0,
								BT_z_return.lastIndexOf("OK"));
						Protocol_in_Frist = "H";
						begiconnect_z = 1;
					}
					// else{
					// BT_z_return =
					// BT_z_return.substring(0,BT_z_return.lastIndexOf("\r\nOK"));
					// begiconnect_z = 1;
					// }
					Log.v("Finish z", BT_z_return);
				} else if (begiconnect_z == 2 && BT_z_return.length() >= 14) {
					if (BT_z_return.substring(0, 14).equals("CAN 11bits 250")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 29bits 250")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 11bits 500")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"CAN 29bits 500")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD ISO9141   ")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD ISO14230  ")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD J1850 PWM ")) {
						begiconnect_z = 0;
					} else if (BT_z_return.substring(0, 14).equals(
							"OBD J1850 VPW ")) {
						begiconnect_z = 0;
					}

					Log.v("Finish z - before STOP", BT_z_return);
				}
				// ERROR
				else if (BT_z_return.length() == 5) {
					if (BT_z_return.substring(0, 5).equals("ERROR")) {
						begiconnect_z = 2;
						Log.v("ERROR", BT_z_return.substring(0, 5));
					}
				}
				// STOP
				else if (BT_z_return.length() == 13) {
					if (BT_z_return.substring(0, 13).equals("OBD SCAN STOP")) {
						begiconnect_z = 2;
						Log.v("STOP",
								BT_z_return.substring(0, BT_z_return.length()));
					}
				}
			} else if (check_ok == 0) {
				again++;
				if (again > 300000000) {
					again = 0;
					begiconnect_z = 2;
					BT_z_return = "No return value";
				}
			}
		}
		return BT_z_return;
	}

	public String Send_Mode01_PIDs_to_OBDII(String PID01XX) {
		sMsgofCmdPID.delete(0, sMsgofCmdPID.length());
		Length = "";
		R_Mode = "";
		R_PID = "";
		Data_Packet = "";
		Final_readMessage = "0";
		check_ok = 0;
		int again = 0;
		// String Send_Mode01_PIDs_to_OBDII = Protocol_in_Frist + "01" + PID01XX
		// + enter;
		String Send_Mode01_PIDs_to_OBDII = Protocol_in_Frist + "01" + PID01XX
				+ enter;
		String Send_Mode01_PIDs_to_OBDII_return = "0";
		int begiconnect_Mode01_PIDs_to_OBDII = 0;

		try {
			pdd.ProtocolTEST(Send_Mode01_PIDs_to_OBDII);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (begiconnect_Mode01_PIDs_to_OBDII == 0) {

			Send_Mode01_PIDs_to_OBDII_return = Final_readMessage;
			if (check_ok > 0) {
				if (begiconnect_Mode01_PIDs_to_OBDII == 0
						&& Send_Mode01_PIDs_to_OBDII_return.length() >= 14) {
					if (Send_Mode01_PIDs_to_OBDII_return.substring(0, 14)
							.equals("CAN 11bits 250")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 1);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 29bits 250")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 2);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 11bits 500")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 3);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 29bits 500")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 4);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD ISO9141   ")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 5);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD ISO14230  ")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 6);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD J1850 PWM ")) {
						Send_Mode01_PIDs_to_OBDII_return = Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.lastIndexOf("OK"));
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 7);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD J1850 VPW ")) {
						Send_Mode01_PIDs_to_OBDII_return = analyze(
								Send_Mode01_PIDs_to_OBDII_return, 8);
						begiconnect_Mode01_PIDs_to_OBDII = 1;
					}
				} else if (begiconnect_Mode01_PIDs_to_OBDII == 2
						&& Send_Mode01_PIDs_to_OBDII_return.length() >= 14) {
					if (Send_Mode01_PIDs_to_OBDII_return.substring(0, 14)
							.equals("CAN 11bits 250")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 29bits 250")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 11bits 500")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("CAN 29bits 500")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD ISO9141   ")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD ISO14230  ")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD J1850 PWM ")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					} else if (Send_Mode01_PIDs_to_OBDII_return
							.substring(0, 14).equals("OBD J1850 VPW ")) {
						begiconnect_Mode01_PIDs_to_OBDII = 0;
					}
					Log.v("Finish z - before STOP",
							Send_Mode01_PIDs_to_OBDII_return);
				}
				// ERROR
				else if (Send_Mode01_PIDs_to_OBDII_return.length() == 5) {
					if (Send_Mode01_PIDs_to_OBDII_return.substring(0, 5)
							.equals("ERROR")) {
						Log.v("ERROR", Send_Mode01_PIDs_to_OBDII_return
								.substring(0, 5));
					}
				}
				// STOP
				else if (Send_Mode01_PIDs_to_OBDII_return.length() == 13) {
					if (Send_Mode01_PIDs_to_OBDII_return.substring(0, 13)
							.equals("OBD SCAN STOP")) {
						begiconnect_Mode01_PIDs_to_OBDII = 2;
						Log.v("STOP", Send_Mode01_PIDs_to_OBDII_return
								.substring(0, Send_Mode01_PIDs_to_OBDII_return
										.length()));
					}
				}
			} else if (check_ok == 0) {
				again++;
				if (again > 300000000) {
					again = 0;
					begiconnect_Mode01_PIDs_to_OBDII = 2;
					Send_Mode01_PIDs_to_OBDII_return = "No return value";
				}
			}
		}
		return Send_Mode01_PIDs_to_OBDII_return;
	}

	public String analyze(String Send_Mode01_PIDs, int Code_in_Frist) {
		String Analysis = "No Support";

		sMsgofCmdPID.append(Send_Mode01_PIDs);

		try {
			mMsg = Packet.Filter(sMsgofCmdPID, Code_in_Frist);
			mLength = mMsg.getData().getByteArray("Length");
			mR_Mode = mMsg.getData().getByteArray("R_Mode");
			mR_PID = mMsg.getData().getByteArray("R_PID");
			mData_Packet = mMsg.getData().getByteArray("Data_Packet");

			Length = new String(mLength);
			R_Mode = new String(mR_Mode);
			R_PID = new String(mR_PID);
			Data_Packet = new String(mData_Packet);
		} catch (Exception e) {
			e.printStackTrace();
		}// end of try

		int Fin_Data = 0;
		if (!Arrays.equals(mLength, check)) {
			int decide_1 = 0;
			Fin_Data = 0;
			// String buffer_2;
			try {
				mmMsg = Packet.Analyze_Data("01" + R_PID, mLength, mR_Mode,
						mR_PID, mData_Packet, 1);
				Log.v("mmMsg",
						String.valueOf("01" + R_PID) + ","
								+ String.valueOf(mLength) + ","
								+ String.valueOf(mR_Mode) + ","
								+ String.valueOf(mR_PID) + ","
								+ String.valueOf(mData_Packet));
				// Mode_PID = mmMsg.getData().getInt("Mode_PID");
				decide_1 = mmMsg.getData().getInt("decide_1");

			} catch (Exception e) {
				e.printStackTrace();
			}// end of try

			Analysis = Data_Packet;

			switch (decide_1) {
			case 0:
				// Analysis = Data_Packet;
				break;
			case 1:

				Fin_Data = (int) mmMsg.getData().getDouble("Fin_Data");

				Analysis = String.valueOf(Fin_Data);
				Log.v("Fin_Data", String.valueOf(Fin_Data));
				// Text_One.setText( String.valueOf(Fin_Data) );

				break;
			case 2:
			case 3:
			case 4:
				break;
			default:
				break;
			}

		} else {
		}
		return Analysis;
	}
}




