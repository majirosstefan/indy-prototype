# indy-prototype - 1.8.2 Indy SDK leads to app crashing

Repository contains source code for the Android Instrumented Test (running on either simulator or real device ) for Indy SDK
 ( https://github.com/hyperledger/indy-sdk) at version 1.8.2 in order to resolve application crashes.

# Description
When debugging, app seems to crash upon creation of Exception as IndyException in constructor creates
new ErrorDetails object that later make additional call to Indy. Indy SDK 1.7.0 is missing that new ErrorDetails()  statement and therefore do not cause app to crash. App was not connected to Ledger Instance.

## Details 

##### IndyException constructor in Indy Java SDK 1.8.2
 <code>

	protected IndyException(String message, int sdkErrorCode) {
		super(message);
		ErrorDetails errorDetails = new ErrorDetails();
		this.sdkErrorCode = sdkErrorCode;
		this.sdkMessage = errorDetails.message;
		this.sdkBacktrace = errorDetails.backtrace;
	}
	
</code>

##### Inside Error Details constructor

<code> 
    
    private ErrorDetails() {
			PointerByReference errorDetailsJson = new PointerByReference();
			LibIndy.api.indy_get_current_error(errorDetailsJson);  // this is making app crash 
			
			try {
				JSONObject errorDetails = new JSONObject(errorDetailsJson.getValue().getString(0));
				this.message = errorDetails.optString("message");
				this.backtrace = errorDetails.optString("backtrace");
			} catch (Exception ignored){
				// Nothing to do
			}
		}
</code> 

## Error message from Logcat 
- included in separate file (at the root of this directory) error_from_logcat.txt. 

## How was the app built 
libs inside src/main/jniLibs were copied from https://repo.sovrin.org/android/libindy/stable/1.8.2/.

libjniDispatch.so's were taken from https://github.com/java-native-access/jna/tree/4.4.0/lib/native android libs(after unpacking).

libgnustl_shared.so's were taken from  the outputs of successful React-Native build(here for simplicity, I included only Android test and copied as few files as it was possible)

Gradle uses implementation 'org.hyperledger:indy:1.8.2' from https://repo.sovrin.org/repository/maven-public".
