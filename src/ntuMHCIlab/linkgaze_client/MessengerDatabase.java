package ntuMHCIlab.linkgaze_client;

import org.json.JSONException;
import org.json.JSONObject;

public class MessengerDatabase {
	MessengerDatabase(){};
	
	public JSONObject queryByTarget_AndSN_(String target, int serialNumber) throws JSONException{
		JSONObject messageReturned = new JSONObject();
		if(target.equals("PEOPLE_IDIOT")){
			//sender: FRIEND:是別人傳的   SELF:自己傳出去的
			//link: BANK:bank app，GPS_FRIEND:由朋友發出的GPS
			messageReturned.put("sender","FRIEND");
			switch(serialNumber){
				case -1:
					messageReturned.put("dialogue", "[LINUX PokoPang波兔村保衛戰]收到來自於JSB的邀請。同心協力一起擊退怪物們吧！");
					break;
				case 0:
					messageReturned.put("dialogue", "[LINUX 跑跑薑餅人] 香甜可口的跑酷遊戲！現在加入開始玩的話，現拿10000金幣呦～快點跟餅乾們一起奔跑吧！");
					break;
				case 1:
					messageReturned.put("dialogue", "[LINUX Rangers]SOS!莎莉有危險了！JSB傳送邀請函來了。趕緊出動去拯救莎莉吧！");
					break;
				case 2:
					messageReturned.put("dialogue", "終於讓我找到了，推薦一個全台灣最優質的網站，這個網站，幾乎全台灣不管大大小小的考試資料都有，比如學測，考取證照，高普考，國考，什麼都有在賣，價位非常低，建議你們去看看");
					break;
				case 3:
					messageReturned.put("dialogue", "美語家教到你家，最有效的英文學習，免出門，在家輕鬆學，限一百名，額滿為止，立即申請！請電04-22347658");
					break;
				case 4:
					messageReturned.put("dialogue", "各位家長們，還在為了小孩的學業煩惱嗎？經過朋友推薦找到優質光碟網站，才讓自己小孩課業進步特別快，建議你們去看看。");
					break;
				case 5:
					messageReturned.put("dialogue", "朋友們，我不是做廣告請不要誤會，我最近找到一個網站，教育，軟體什麼光碟都有，推薦你去看，賣的很便宜。");
					break;
				case 6:
					messageReturned.put("dialogue", "hi baby 推薦一個麻吉賣家 價格超低 質量還不錯 服務超正 都貨到付款 他的衣服*鞋*包*皮夾*妝組*香水*手錶都很好  line:ilove5566");
					break;
				default:
					break;
			}
		}else if(target.equals("PEOPLE_HR")){
			messageReturned.put("sender","FRIEND");
			switch(serialNumber){
				case -1:
					messageReturned.put("dialogue", "花旗銀行客戶您好，您的帳戶2004*****3451有所變更，請前往專屬App查詢確認，謝謝您。");
					messageReturned.put("link", "BANK_APP");
					break;
				case 0:
					messageReturned.put("dialogue", "全臺超過1,500家，花旗「品味饗宴」最優5折精選美饌。活動期間：即日起~2014/12/31");
					break;
				case 1:
					messageReturned.put("dialogue", "花旗銀行客戶您好，您的帳戶2004*****3451有所變更，請前往專屬App查詢確認，謝謝您。");
					messageReturned.put("link", "BANK_APP");
					break;
				default:
					break;
			}
	
		}else if(target.equals("PEOPLE_FRIEND")){
			switch(serialNumber){
				case 0:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "但是他超宅耶 而且鬍子都沒刮超噁!!!");
					break;
				case 1:
					messageReturned.put("sender","SELF");
					messageReturned.put("dialogue", "哪有你宅XD");
					break;
				case 2:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "聽你在那邊~~ 至少我沒鬍子好嗎XDDD");
					break;
				case 3:
					messageReturned.put("sender","SELF");
					messageReturned.put("dialogue", "什麼啦XD 欸有點晚了我先睡哦");
					break;
				case 4:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "才十一點你在體虛什麼啦 廢耶~~");
					break;
				case 5:
					messageReturned.put("sender","SELF");
					messageReturned.put("dialogue", "哈哈這你就不知道了，先走啦掰~");
					break;
				case 6:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "好你早點休息啦~ 掰囉");
					break;
				case 7:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "要吃飯嗎~~~");
					break;
				case 8:
					messageReturned.put("sender","FRIEND");
					messageReturned.put("dialogue", "吃這家~~~直接十分鐘後那邊見吧~~");
					messageReturned.put("link", "GPS_FRIEND");
					break;
	
				default:
					break;
			}
		}
		
		if(messageReturned.isNull("link")){
			messageReturned.put("link", "N/A");;
		}
		return messageReturned;
	}
}
