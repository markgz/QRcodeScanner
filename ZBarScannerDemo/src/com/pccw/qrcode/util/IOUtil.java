package com.pccw.qrcode.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.pccw.qrcode.dao.UserInfo;

public class IOUtil {

	private Context context;

	public IOUtil(Context context) {

		this.context = context;
	}

	private static final String TAG = IOUtil.class.getSimpleName();

	public void moveFileToSDCard(String storeDirectoryPath,String fileName) {

		InputStream fis = null;
		FileOutputStream fos = null;
		
		File tempFile = new File(storeDirectoryPath+"/"+fileName);
		
		if(tempFile.isFile()&&!tempFile.exists()){
			
			Log.i(TAG, "File existed don't need to move from assets directory");
		}else{
			
			try {
				fis = context.getAssets().open(fileName);

				File file = new File(storeDirectoryPath);

				file.mkdirs();
				
				fos = new FileOutputStream(storeDirectoryPath+"/"+fileName);

				byte[] buffer = new byte[1024];

				int len = 0;

				while ((len = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		
	}

	public String readFile(String path) {

		String allText = "";

		File file = new File(path);

		if (file.isFile() && file.exists()) {

			try {
				InputStream fis = new FileInputStream(file);

				InputStreamReader inputStreamReader = new InputStreamReader(fis);

				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				String lineText = "";

				while ((lineText = bufferedReader.readLine()) != null) {

					allText += lineText + "\n";
				}

				bufferedReader.close();
				inputStreamReader.close();
				fis.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.e(TAG, "file isn't file or doesn't exist,please check whether the path is correct");
		}

		return allText;
	}

	public Map<String,UserInfo> parseUserInfo(String allText){
		Map<String,UserInfo> userInfoList = new HashMap<String,UserInfo>();
		//#id:10654596&username:markyan&email:markyan@pccw.com&phone:123456&votes:pccw/tecent&likes:dell/sun/microsoft
		
		//if(allText.startsWith("#")){
			
			String[] linesArray = allText.split("\n");
			
			for (String lineStr : linesArray) {
				
				UserInfo userInfo = new UserInfo();
				String[] userInfoArray = lineStr.split(",");
				
				userInfo.setId(userInfoArray[0]);
				userInfo.setUsername(userInfoArray[1]);
				userInfo.setEmail(userInfoArray[2]);
				
				userInfo.setPhone(userInfoArray[3]);
				
				String votesStr = userInfoArray[4];
				
				String[] votesStrArray = votesStr.split("/");
				
				List<String> votes = new ArrayList<String>();
				
				for (String vote : votesStrArray) {
					
					votes.add(vote);
				}
				
				userInfo.setVotes(votes);
				
				String likesStr = userInfoArray[5];
				
				String[] likesStrArray = likesStr.split("/");
				
				List<String> likes = new ArrayList<String>();
				
				for (String like : likesStrArray) {
					
					likes.add(like);
				}
				
				userInfo.setLikes(likes);
				
				
				
				/*for (String string : userInfoArray) {
					
					if(string.startsWith("#id")){
						
						String[] strSplit = string.split(":");
						userInfo.setId(strSplit[1]);
						
					}else if(string.startsWith("username")){
						
						String[] strSplit = string.split(":");
						userInfo.setUsername(strSplit[1]);
						
					}else if(string.startsWith("email")){
						String[] strSplit = string.split(":");
						userInfo.setEmail(strSplit[1]);
						
					}else if(string.startsWith("phone")){
						String[] strSplit = string.split(":");
						userInfo.setPhone(strSplit[1]);
					}else if(string.startsWith("votes")){
						
						String[] strSplit = string.split(":");
						String[] votesStr = strSplit[1].split("/");
						
						List<String> votes = new ArrayList<String>();
						for (String vote : votesStr) {
							votes.add(vote);
						}
						
						userInfo.setVotes(votes);
					}else if(string.startsWith("likes")){
						String[] strSplit = string.split(":");
						
						String[] likesStr = strSplit[1].split("/");
						
						List<String> likes = new ArrayList<String>();
						for (String like : likesStr) {
							likes.add(like);
						}
						userInfo.setLikes(likes);
					}
					
				}*/
				userInfoList.put(userInfo.getId(), userInfo);
			}
			
			
			
		//}else{
			
		//	Log.e(TAG, "only parse String with fixed format eq. started with #");
		//}
		
		return userInfoList;
		
	}
}
