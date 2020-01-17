package cordova.plugin.biometricauth;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;


import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List; 

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ozforensics.liveness.sdk.actions.model.OzDataResponse;
import com.ozforensics.liveness.sdk.activity.CameraActivity;
import com.ozforensics.liveness.sdk.network.manager.NetworkManager;
import com.ozforensics.liveness.sdk.utility.enums.Action;
import com.ozforensics.liveness.sdk.utility.enums.OzApiRequestErrors;
import com.ozforensics.liveness.sdk.utility.enums.OzApiStatusVideoAnalyse;
import com.ozforensics.liveness.sdk.utility.enums.ResultCode;
import com.ozforensics.liveness.sdk.utility.managers.OzLivenessSDK;
import com.ozforensics.liveness.sdk.actions.model.OzMediaResponse;
import com.ozforensics.liveness.sdk.network.manager.UploadAndAnalyzeStatusListener;
import com.ozforensics.liveness.sdk.network.manager.LoginStatusListener;
import com.ozforensics.liveness.sdk.utility.enums.NetworkMediaTags;
import com.ozforensics.liveness.sdk.actions.model.LivenessCheckResult;

/**
 * This class echoes a string called from JavaScript.
 */
public class BiometricAuth extends CordovaPlugin {
	
	private CallbackContext mCallbackContext;
	private String path;
	
    private UploadAndAnalyzeStatusListener analyzeStatusListener = new UploadAndAnalyzeStatusListener() {

        @Override
        public void onSuccess(@NotNull List<LivenessCheckResult> result, @Nullable String stringInterpretation) {
            //if (stringInterpretation != null) showHint(stringInterpretation);
			mCallbackContext.success(stringInterpretation);
        }

        @Override
        public void onStatusChanged(@Nullable String status) {
            //if (status != null) showHint(status);
			
        }

        @Override
        public void onError(@NotNull List<LivenessCheckResult> result, @NotNull String errorMessage) {
            //showHint(errorMessage);
			mCallbackContext.error(errorMessage);
        }
    };

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//Log.d("BiometricAuth", "teeest");
		mCallbackContext = callbackContext;
        if (action.equals("testBiometric")) {
            path = args.getString(0);
            this.testBiometric(callbackContext);
            return true;
        }
        return false;
    }

    private void testBiometric(CallbackContext callbackContext) {
        /*if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        } */

        //ArrayList<Action> actions = new ArrayList<Action>();
        //actions.add(Action.EyeBlink);
		//OzLivenessSDK.actions = actions;
		
		//Intent intent = new Intent(this, CameraActivity.class);
        //startActivityForResult(intent, OzLivenessSDK.requestCode);
		
		//List<OzLivenessSDK.OzAction> actions = new ArrayList<>();
		//actions.add(OzLivenessSDK.OzAction.Smile);
		//actions.add(OzLivenessSDK.OzAction.Scan);
		//Intent intent = OzLivenessSDK.INSTANCE.createStartIntent(this.cordova.getActivity(), actions, 3, 3, true, null, null);
		//this.cordova.startActivityForResult(this, intent, 5);		
		//callbackContext.success("success123");
		final CordovaPlugin that = this;
		LoginStatusListener loginStatusListener = new LoginStatusListener() {
            @Override
            public void onSuccess(@NotNull String token) {
                	List<OzLivenessSDK.OzAction> actions = new ArrayList<>();
					actions.add(OzLivenessSDK.OzAction.Smile);
					actions.add(OzLivenessSDK.OzAction.Scan);

					Intent intent = OzLivenessSDK.INSTANCE.createStartIntent(that.cordova.getActivity(), actions, 3, 3, true, null, null);
					that.cordova.startActivityForResult(that, intent, 5);
					
					//callbackContext.success("success123");
            }

            @Override
            public void onError(int errorCode, @NotNull String errorMessage) {
                callbackContext.error(errorMessage);
            }
        };
		
		//Intent intent = this.cordova.getActivity().getIntent();
		//String api = intent.getStringExtra("API_URL");
		//String username = intent.getStringExtra("USERNAME"); 
		//String password = intent.getStringExtra("PASSWORD"); ? change to initalize?
		Context context = this.cordova.getActivity();
		String packageName = context.getPackageName();
		String api = context.getString(resources.getIdentifier("api_url", STRING, packageName));
		String username = context.getString(resources.getIdentifier("username", STRING, packageName));
		String password = context.getString(resources.getIdentifier("password", STRING, packageName));
        OzLivenessSDK.INSTANCE.login(this.cordova.getActivity().getApplicationContext(), api, username, password, loginStatusListener);
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        String error = OzLivenessSDK.INSTANCE.getErrorFromIntent(data);
        //if (error != null) showHint(error);

        List<OzMediaResponse> sdkMediaResult = OzLivenessSDK.INSTANCE.getResultFromIntent(data);
		
		/*
		//mCallbackContext.success(sdkMediaResult);
		try {
			String jsonString = new Gson().toJson(sdkMediaResult);
			JSONObject mJSONObject = new JSONObject(jsonString);
			PluginResult result = new PluginResult(PluginResult.Status.OK, mJSONObject);
			mCallbackContext.sendPluginResult(result);
		} catch (JSONException e) {
            //e.printStackTrace();
        }
		*/
		
        //if (resultCode == RESULT_OK) {
            uploadAndAnalyze(sdkMediaResult);
        //}
    }
	
	private void uploadAndAnalyze(List<OzMediaResponse> mediaList) {
        if (mediaList != null) {
			//"storage/emulated/0/download/doc.png"
			//String path = "data/user/0/am.prometeybank.mobilebank/files/doc.png";
			mediaList.add(new OzMediaResponse(OzMediaResponse.Type.PHOTO, path, NetworkMediaTags.PhotoIdFront));
            OzLivenessSDK.INSTANCE.uploadMediaAndAnalyze(
                    this.cordova.getActivity().getApplicationContext(),
                    mediaList,
                    analyzeStatusListener
            );
        }
    } 
}
