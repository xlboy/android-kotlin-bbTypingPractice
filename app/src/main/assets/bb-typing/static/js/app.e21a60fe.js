(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["app"],{0:function(e,t,r){e.exports=r("56d7")},"56d7":function(e,t,r){"use strict";r.r(t);var n={};r.r(n),r.d(n,"formatDate",(function(){return Q})),r.d(n,"hidePhone",(function(){return F}));var a=r("80a0"),o=(r("a133"),r("ed0d"),r("f09c"),r("e117"),r("e3a0"),r("b449"),r("0261")),i=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app",attrs:{id:"app"}},[r("input",{attrs:{type:"text",id:"shareText"}}),r("div",{staticClass:"stm"}),r("keep-alive",[e.$route.meta.keepAlive?r("router-view"):e._e()],1),e.$route.meta.keepAlive?e._e():r("router-view")],1)},u=[],s={name:"App",components:{},data:function(){return{}}},d=s,c=r("4023"),l=Object(c["a"])(d,i,u,!1,null,null,null),p=l.exports,f=(r("e18c"),r("1bee"));o["default"].use(f["a"]);var g=[{path:"/",name:"Index.vue",component:function(){return r.e("chunk-08a2ac6c").then(r.bind(null,"7abe"))},meta:{title:"首页",keepAlive:!1}}],m=function(){return new f["a"]({scrollBehavior:function(){return{y:0}},routes:g})},h=m(),v=r("9660"),b={userName:function(e){return e.app.userName}},y=b,x={userName:""},w={SET_USER_NAME:function(e,t){e.userName=t}},S={setUserName:function(e,t){var r=e.commit;r("SET_USER_NAME",t)}},k={state:x,mutations:w,actions:S},A={style:{pageStyle:{backOption:70,top:"rgba(255, 255, 255, 0.7)",bottom:"rgba(222, 222, 255, 0.7)"}},isShow:{isWordHint:!1,isBindQQ:!1,isSign:!1,rigDrawer:!1,isWordAnalysis:!1},articleAry:{ckAry:[],textAry:[],textAry2:[]}},T={state:A};o["default"].use(v["a"]);var E=new v["a"].Store({modules:{app:k,typing:T},getters:y}),$=E,R=r("b0ea"),D=r.n(R),N=(r("9536"),r("f121")),W=r("82ae"),P=r.n(W),C="http://xlboy.cn:8988",H={post:function(e,t){return new Promise((function(r){P.a.post("".concat(C).concat(e),t).then((function(e){r(e.data)}))}))}};o["default"].prototype.$axios=H;var I="production";o["default"].prototype.toast=function(e){if("development"===I)return alert(e);androids.message(e)},o["default"].prototype.loginIptBlur=function(e){2===e?androids.inputFocus():androids.inputBlur()},o["default"].prototype.isNull=function(e){return""===e||null===e||void 0===e},o["default"].prototype.shuffle=function(e){var t,r,n=e.length;while(n)r=Math.floor(Math.random()*n--),t=e[n],e[n]=e[r],e[r]=t;return e},o["default"].prototype.convertCanvasToImage=function(e){return e.toDataURL("image/png")};r("3882");var J=r("5c66"),L=(r("efa9"),r("e790")),M=(r("7d9c3"),r("0a2d")),_=(r("5444"),r("64ad")),z=(r("66ca"),r("76a0"));o["default"].use(z["a"]),o["default"].use(_["a"]),o["default"].use(M["a"]),o["default"].use(J["a"]).use(L["a"]);var B=r("a3c5"),O=r.n(B);r("b9b6");o["default"].use(O.a),o["default"].prototype.getLoading=function(){var e=O.a.Loading.service({lock:!0,spinner:"el-icon-loading",background:"rgba(255,255,255, 0.8)"});return e};var U=r("7d2c"),j=r.n(U);o["default"].use(j.a);r("44ce"),r("dbb3"),r("fe59"),r("fe8a"),r("08ba"),r("84c2"),r("e35a"),r("1c2e8"),r("5e9f");function Q(e,t){if(void 0!==e){var r=new Date(e);/(y+)/.test(t)&&(t=t.replace(RegExp.$1,(r.getFullYear()+"").substr(4-RegExp.$1.length)));var n={"M+":r.getMonth()+1,"d+":r.getDate(),"h+":r.getHours(),"m+":r.getMinutes(),"s+":r.getSeconds()};for(var a in n)if(new RegExp("(".concat(a,")")).test(t)){var o=n[a]+"";t=t.replace(RegExp.$1,1===RegExp.$1.length?o:q(o))}return t}}function q(e){return("00"+e).substr(e.length)}function F(e){return e.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")}Object.keys(n).forEach((function(e){return o["default"].filter(e,n[e])})),o["default"].prototype.$formatDate=o["default"].filter("formatDate"),o["default"].prototype.$hidePhone=o["default"].filter("hidePhone"),o["default"].prototype.$cdn=N["$cdn"],o["default"].config.productionTip=!1,new o["default"]({el:"#app",router:h,store:$,data:Object(a["a"])(Object(a["a"])({},D.a),{},{eventHub:new o["default"]}),render:function(e){return e(p)}})},9536:function(e,t,r){},b0ea:function(e,t,r){var n=r("d7f7"),a={userId:"",qq:1,avatarAddress:"",sign:"这个人很懒...什么都没有设置",userName:"",currentWordId:1,countSize:0,twoWord:"rgba(62, 0, 0, 0.96)",threeWord:"rgba(62, 110, 0, 0.96)",fourWord:"rgba(62, 0, 110, 0.96)",fourC:"rgba(62, 110, 110, 0.96)"},o={start:0,backChange:0,zjs:"",speed:0,runningYard:0,keystroke:0,isChn:!0,previousBm:"",wordStr:"",backSpace:0,realTimeSpeed:[]};e.exports={default:{user:n({},a),sszk:n({},o)},diyWordJson:{wordTitle:"",wordText:"",lastTextLength:0},textJson:{dealWithText:"",tR:"",tRLength:0,hintAry:{ckHintAry:[],isCorrectAry:[],yesHint:[],noHint:[]}},userInfo:n({},a),gitTyping:{currentSeverNum:1,currentText:"",currentMaxSize:100},gdqJson:{llmc:1,tempStartTime:null,pauseTime:0,startTime:null,endTime:null,severnNum:1,withStr:"如果我是DJ你会爱我吗",currentWordHint:""},currentPage:{currentDaySize:0,startStrLength:0,currentPageNum:0,onePageSize:200},isState:{isDiyExercise:!1,isWordHint:!1,isBlur:!1,isSign:!1,isBindQQ:!1,isWordAnalysis:!1,isDrawer:!1,isRegLoginDialog:!1,isReadTextWay:"2",isGradeList:!1,isRradeCharts:!1},wordJson:{optionAry:[],userAry:[],defaultAry:[],id:0,analysisJson:{"全概":[]}},style:{pageStyle:{backOption:70,top:"rgba(255, 255, 255, 0.7)",bottom:"rgba(222, 222, 255, 0.7)"}},sszk:n({},o)}},f121:function(e,t,r){var n=r("f1e0");e.exports=n},f1e0:function(e,t){e.exports={title:"vue-h5-template",baseUrl:"https://www.xxx.com/",baseApi:"https://www.xxx.com/api",APPID:"xxx",APPSECRET:"xxx",$cdn:"https://imgs.solui.cn"}}},[[0,"runtime","chunk-vantUI","chunk-libs"]]]);