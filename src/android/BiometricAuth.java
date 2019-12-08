package cordova.plugin.biometricauth;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import java.util.ArrayList; 

import com.ozforensics.liveness.sdk.actions.model.OzDataResponse;
import com.ozforensics.liveness.sdk.activity.CameraActivity;
import com.ozforensics.liveness.sdk.network.manager.NetworkManager;
import com.ozforensics.liveness.sdk.utility.enums.Action;
import com.ozforensics.liveness.sdk.utility.enums.OzApiRequestErrors;
import com.ozforensics.liveness.sdk.utility.enums.OzApiStatusVideoAnalyse;
import com.ozforensics.liveness.sdk.utility.enums.ResultCode;
import com.ozforensics.liveness.sdk.utility.managers.OzLivenessSDK;

/**
 * This class echoes a string called from JavaScript.
 */
public class BiometricAuth extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
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

        ArrayList<Action> actions = new ArrayList<Action>();
        actions.add(Action.EyeBlink);
        OzLivenessSDK sdk = new OzLivenessSDK();
		sdk.setActions(actions);
		
		Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, sdk.getRequestCode());
		
		callbackContext.success("success");
    }
}
