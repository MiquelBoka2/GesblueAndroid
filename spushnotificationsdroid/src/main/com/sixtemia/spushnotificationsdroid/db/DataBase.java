/**
   Copyright Mob&Me 2013 (@MobAndMe)

   Licensed under the GPL General Public License, Version 3.0 (the "License"),  
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.gnu.org/licenses/gpl.html

   Unless required by applicable law or agreed to in writing, software 
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
   Website: http://adaframework.com
   Contact: Txus Ballesteros <txus.ballesteros@mobandme.com>
*/

package com.sixtemia.spushnotificationsdroid.db;

public class DataBase {
	public static DataContext Context;
	
	/**
	 * This method initialize the singleton instance of Application ObjectContext.
	 * @param pContext
	 */
	public static void initialize(android.content.Context pContext) {
		if (Context == null) {
			DataContext.mContext = pContext;
			Context = new DataContext(pContext);
		}
	}
}
