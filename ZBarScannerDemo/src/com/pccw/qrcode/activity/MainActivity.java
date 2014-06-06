package com.pccw.qrcode.activity;

import java.util.Map;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.pccw.qrcode.dao.UserInfo;
import com.pccw.qrcode.util.IOUtil;

public class MainActivity extends Activity {

	private static final int ZBAR_QR_SCANNER_REQUEST = 0;
	
	private TextView userInfoTextView;
	
	private Button goToVoteButton;
	
	private String scanResult;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		userInfoTextView = (TextView) findViewById(R.id.user_info_textview);
		
		goToVoteButton = (Button) findViewById(R.id.go_to_vote_button);
		
		
	}

	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void goToVote(View v){
		
		Intent intent = new Intent(this, VoteAndLikesActivity.class);
		intent.putExtra("scanResult", scanResult);
		startActivity(intent);
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				//Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
				
				scanResult = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				
				IOUtil ioUtil = new IOUtil(this);
				
				String state = Environment.getExternalStorageState();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					
					String storeDirectoryPath = Environment.getExternalStorageDirectory()+"/qrcode";
					String fileName = getResources().getString(R.string.userInfo);
					ioUtil.moveFileToSDCard(storeDirectoryPath,fileName);
					
					String userInfoText = ioUtil.readFile(storeDirectoryPath+"/"+fileName);
					
					Map<String,UserInfo> userInfoMap = ioUtil.parseUserInfo(userInfoText);
					
					if(userInfoMap.get(scanResult)!=null){
						
						String userInfo = userInfoMap.get(scanResult).toString();
						
						userInfoTextView.setText(userInfo);
						
						goToVoteButton.setVisibility(View.VISIBLE);
						
					}else{
						
						userInfoTextView.setText("Sorry,cannot find you in our records!");
					}
					
			}
				
				
				
			} else if (resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if (!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
}
