package cordova.plugin.biometricauth;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.Plugin;
import org.apache.cordova.PluginResult;


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

/**
 * This class echoes a string called from JavaScript.
 */
public class BiometricAuth extends CordovaPlugin {
	
	private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//Log.d("BiometricAuth", "teeest");
		mCallbackContext = callbackContext;
        if (action.equals("testBiometric")) {
            String message = args.getString(0);
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
		
        OzLivenessSDK.INSTANCE.login(this.cordova.getActivity().getApplicationContext(), "https://api-d.oz-services.ru/", "Artur.kartshikyan@evocabank.am", "g9Ub@dP7$am", loginStatusListener);
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        String error = OzLivenessSDK.INSTANCE.getErrorFromIntent(data);
        //if (error != null) showHint(error);

        List<OzMediaResponse> sdkMediaResult = OzLivenessSDK.INSTANCE.getResultFromIntent(data);
		
		//mCallbackContext.success(sdkMediaResult);
		
		 PluginResult result = new PluginResult(PluginResult.Status.OK, sdkMediaResult);
		 mCallbackContext.sendPluginResult(result);
		
        //if (resultCode == RESULT_OK) {
        //    uploadAndAnalyze(sdkMediaResult);
        //}
    }
}
