package MyExeptions;

public class MyExeptionBase extends Exception {
	public MyExeptionBase(String mes,String typeOfError) {
		super(typeOfError + mes);
		mes = typeOfError + mes;
		LogWriter.WriteLog(mes);
	}
	
}
