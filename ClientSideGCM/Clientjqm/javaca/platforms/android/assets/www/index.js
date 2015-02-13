var playerID= "";
var phoneNumber="";
var ready=false;
var numberofTries="";
var oldplayer=false;
var playername = "";
var regId="";
var toPHoneNumber="";
var defaultmsg="I have found a awesome game!!if you have guts , try to beat me  "
$.when(jqmReady,cordovaReady).done(function(){
    console.log("Testing >> Cordova IME" + cordova.plugins.uid.IMEI);
    playerID=cordova.plugins.uid.IMEI;
    
    console.log("Navigator>>>>>>>>>>>>>>>>>>>");
        console.log(navigator.contacts);
    readyLoaded= true;
    ready=true;
   //  $.mobile.loading("hide");
    console.log("All ready.,...");
    // send the uiid to server and check weather this user is already existed or not..???
    if(playerID!="")
    {
        
        
        // check and verfit the data from the db for  imei number
     var promise = $.getJSON(url+"/api/gamePlay/checkoldplayer",{uid:playerID});
        promise.done(function (data){
        oldplayer=true;
        playername=data.name;
            //GCM
            phoneNumber=data.phoneNumber;
            
            console.log("player name from check old player json>>>"+playername+">>>player id"+playerID);
        numberofTries=data.numberoftries;
            
             registerGCMServer();
            console.log("Registration ID"+regId);
        
        });
        promise.fail(function(){
                  console.log("failed. in checking old player....");   
                  playername=""; 
                
                     });
        
        
        
   
    }
    
   
    
    

    
    //
   $.mobile.navigate("#newPlayer",{transition:"flip",rel:"back"});

});


function registerGCMServer()
{
  console.log("inside the register method GCM");   
      window.plugins.pushNotification.register(Gcmsuccess, Gcmfail, {
        senderID: "682017053364",
        ecb: "onGCMMessage"
    });
}



function Gcmsuccess() {
    console.log("success");
    
    console.log("Registerd the GCM file ");
}
function Gcmfail() {
    console.log("fail");
}

function onGCMMessage(evt) {
     console.log("Event registran !!!! Completed GCM"+ evt.regid)
    switch (evt.event) {
        case "registered":
            $("#regid").text(evt.regid);
            regId=evt.regid;
            $.post("http://10.10.2.170:8080/gcmpush/register", {
                cmd: "register",
                regId: evt.regid,
                name: playername,
                phone: phoneNumber
            }).done(function() {
    
                console.log(">>> registered with server");
            }).fail(function() {
                console.log(">> fail");
            });
            console.log("Registrration id"+evt.regid);
            break;
            
        case "message":
            console.log(">> message = " 
                        + JSON.stringify(evt.payload.message));
            $("#data").text("from: " + evt.payload.from + " - " + evt.payload.message
                           + " - jsondata: " + JSON.stringify(evt.payload.jsonData));
            console.log(">> json data 123 = " + JSON.stringify(evt.payload.jsonData));
            console.log("Json Data");
            console.log(evt.payload.message);
            console.log("Json Data>>>>>"+evt.payload.jsonData);
            console.log("Json Data inside,..>>>>>"+evt.payload.jsonData.gamename);
            var frGame = evt.payload.jsonData;
            
            
            
           // console.log("frGame"+frGame[0].gamename);
           //  console.log("frGame"+frGame[0]);
            console.log(frGame.gamename);
            console.log("playerName");
            console.log(playername);
            
            Currentgame=frGame.gamename;
             console.log("gameName"+Currentgame);
            $.mobile.navigate("#gamescreen",{transition:"flip",rel:"back"});
            // redirect to game screen....
            // bala
            break;
            
        default:
    }

}









var btnpressed = [];
var uniqImg = [];
var imgCounter = 0;

var Currentgame = ""
var gameSse;
var Playerjson = {"gameSequence": ["1", "2", "3", "4", "7", "8", "9", "10", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "2", "3", "4", "5"]};
var btnonetwo=[];
var lastItemToFlip;


var url = "http://10.10.1.149:8080/javacatest-master";
//var url = "http://192.168.1.13:8080/javacatest-master";
//var url = "http://localhost:8080/javacatest-master";
//var url = "http://10.10.1.148:8080/javacatest-master";
/*

function flip(selector)
{
    $(selector).addClass("flipped");

    $(selector).removeClass("flipped");
}
*/



$(document).on("pagecontainerbeforeshow", function (_, $ui)
{
    //

    if ("GameList" == $ui.toPage.attr("id")) {
        console.log("player ID /*soon IMEI number*/" + playername);
        
        $("#listgames").empty();
        $("#usernametxt").text(playername);
        //$("#listgames").empty();

        var promise = $.getJSON(url + "/api/gameService/getGames");
        promise.done(function (data) {
            for (var i = 0, len = data.length; i < len; i++) {
               var playercount=" (" +data[i].PlayersCount+ ")";
                $("#listgames").append(createListItem(i, data[i].Name + playercount ));
                // $("#listgames").listview("refresh");
              
            }
            $("#listgames").listview("refresh");
        });
    }
    function createListItem(item, value) {

        var $a = $("<a>").attr("href", "#gamescreen");
        $a.text(value);
        //   var $li = $("<li>").append($a);

        // console.log($li);

        var $li = $("<li>").append($a);
        return ($li);
        return $a;
    }


    if ("gamescreen" == $ui.toPage.attr("id"))

// Game screen before show starts here
    {
        //  $(".card").toggleClass("flipped");

        //sSe broadcaster is already created
        
        $("#numberoftries").text(numberofTries+"/ 10");
        
        gameSse = new EventSource(url + "/api/gamePlay/" + Currentgame + "/" + playerID);
        //    gameSse.onmessage = messageHandler;
        gameSse.addEventListener(Currentgame, messageHandler);
        //disable button for waiting respone from sse server  
        $("#submit").attr("disabled", "");
       $("#reset").attr("disabled", "");

  // to test game ssee connection starts
        gameSse.addEventListener('message', function (e) {
            console.log("E ID" + e.id);
            console.log("E Event" + e.event);
            console.log("Data " + e.data);
        }, false);


        gameSse.addEventListener('open', function (e) {
            console.log("SSe conntection is opened ");
        }, false);

        gameSse.addEventListener('error', function (e) {
            if (e.readyState == EventSource.CLOSED) {
                console.log("SSE conntection is opened");
            }
        }, false);
// to test game ssee connection ends
//executed on every success cards...
        function messageHandler(message)
        {
            Playerjson = JSON.parse(message.data);
            console.log(Playerjson);
            console.log("Inside SEE first sent mesage " + Playerjson.gameSequence[0]);

            $('.imgbtn').empty();
           // $("#submit").attr("disabled", "");
        //    $("#reset").attr("disabled", "");
            $('.imgbtn').each(function (i, obj) {
            //console.log($(this));
               // $(this).attr({"class": "ui-btn imgbtn "});
                $(this).append(createImg(Playerjson.gameSequence[i],$(this).attr("id")));
            });
            
            
            var parentArray =[];
            Playerjson.WinningSequence.forEach(function (entry) {
                
                console.log("Winning items" + entry);
                $(".deck" + entry).toggleClass("flipped");
                //lastItemToFlip = $(".deck" + entry);
                // TO REMOVe clickable property of the each card which are winned
                //$(".deck" + entry).parent().off("click");
                var parent  = $(".deck" + entry).parent();
                parentArray = $(".deck" + entry).parent();
               // parent.off("click");
                //parent.removeClass("imgbtn");
                parent.removeAttr("style");
                  parentArray.eq(0).removeAttr("disable");
                  parentArray.eq(1).removeAttr("disable");
                parentArray.eq(0).off("click");
                  parentArray.eq(0).off("click");
                parentArray.eq(1).off("click")
               // parentArray[0].off("click");
               // parentArray[1].off("click");
                console.log(parentArray.eq(0).attr("id") +">>>>>>>>>>"+
                            parentArray.eq(1).attr("id"));
          
            // to make winned card not clickable....start here.
                
                var id1=parentArray.eq(0).attr("id");
                var id2=  parentArray.eq(1).attr("id");
                $("#"+id1).addClass("winned");
                $("#"+id2).addClass("winned");
                //$("#"+id1).removeClass("imgbtn");
                //$("#"+id2).removeClass("imgbtn");
         
            // to make winned card not clickable....ends here.  
      
                //$(".deck"+entry).toggleClass("flipped");
                parentArray=[];
                 //parent.off("click");
                 //parent.removeAttr("disabled");
               
                   
                    
            });
            
            // flush uniqimage array
            uniqImg=[];
           reset();
            //$(".card").toggleClass("flipped");
                

        }



        function createImg(name,btnId) {
            var $front = $("<figure>").attr("class", "front");
            var $back = $("<figure>").attr("class", "back");

            //web
            /*            var $frontimg = $("<img>").attr({"src": "ImagesCards/" + name + ".png", "style": "height:200px;width:150px;"});*/
            
            //mobile
          var $frontimg = $("<img>").attr({"src": "ImagesCards/" + name + ".png", "style": "height:100px;width:50px;"});
            //test
            
            //web
      /*       var $backimg = $("<img>").attr({"src": "ImagesCards/" + "backimage" + ".jpg", "style": "height:200px;width:150px;"});*/
            
            //mobile
            var $backimg = $("<img>").attr({"src": "ImagesCards/" + "backimage" + ".jpg", "style": "height:100px;width:50px;"});
            ////test
//            var $img = $("<img>").attr({"src": "ImagesCards/" + name + ".png"});
            var $div = $("<div>").attr({"class":"card " + "deck" + name,"id":name+btnId});
            $front.append($backimg);
             $back.append($frontimg);
            
            $div.append($back);
            $div.append($front);
            //mobile width..
            //var $img = $("<img>").attr({"src": "ImagesCards/" + name + ".png", "style": "height:100px;width:150px;"});
            return ($div);
        }

// Game screen before show ends here
    }
    
    //new Game page
    if ("newGame" == $ui.toPage.attr("id"))
    {

        $("GameName").val("");
    }
    
    
    
    
          
    
    
      if ("newPlayer" == $ui.toPage.attr("id"))
    {

        console.log("player name set in page before show >>>"+ playername);
        $("#PlayerName").val(playername);
        $("#PlayerPhone").val(phoneNumber);

    }
    






});


$(document).on("pagecontainershow", function (_, $ui)
{

     console.log(">>>container show....");
     // loading page...
    if ("LoadingScreen" == $ui.toPage.attr("id")) {

            $.mobile.loading( 'show', {
                text: 'foo',
                textVisible: true,
                theme: 'z',
                html: ""
            });
        
        
           
            // start the loading screen.

            console.log(" playerID > inside loading screen...ime number"  + playerID);
            // navigaet to   

           // $.mobile.navigate("#newPlayer");

           
            console.log(">>>playerID not set so far... ");
            
        
        
    }

    
    
    // game list page 
    if ("GameList" == $ui.toPage.attr("id")) {



        $('#listgames').on('click', 'li a', function () {
            //    alert($(this).text()+ "this"); // id of clicked li by directly accessing DOMElement property
            var selectpath = $(this).text();
            var res=selectpath.split(" ");
            Currentgame =res[0];
            console.log("select path"+ selectpath+"cureent game"+Currentgame);
            
            $.mobile.navigate("#gamescreen",{transition:"flip",rel:"back"});
        });



        $("#NewGameButton").on("click", function () {
            $.mobile.navigate("#newGame",{transition:"flip",rel:"back"});
        });
        
        

        
        
        
        
        
    }

    if ("newPlayer" == $ui.toPage.attr("id")) {
        
        /*
            $.mobile.loading( 'show', {
                text: 'foo',
                textVisible: true,
                theme: 'b',
                html: ""
             });*/
        
            console.log(">>>playerName inside new player>>"+playername);
           
        
        if(playername!="")
        {//old player
        console.log("player name is ok>>>"+ playername);
            $("#PlayerName").val(playername);
            $("#PlayerName").attr('readonly',true);
            $("#PlayerPhone").val(phoneNumber);
             $("#PlayerPhone").attr('readonly',true);

            
        }
        else
        {
              console.log("player name is null >>>"+ playername);
       

            // new player
          //  $("#PlayerName").attr("");
             
        }
        $("#Navibutton").on("click",function(){
            console.log("Insode contacts>>>>>>>>>>>>>>>>>");
            navigator.contacts.pickContact(function(contact){
                
                var arr = contact.phoneNumbers;
                console.log("phonenumbers");
                console.log(arr);
                console.log("Phone NUmber >>" )
                console.log(arr[0].value);
                
        console.log('The following contact has been selected:' + JSON.stringify(contact));
    },function(err){
        console.log('Error: ' + err);
    });  
        });
        
        $("#startButton").on("click", function () {

            phoneNumber=$("#PlayerPhone").val();
            console.log("Phone NUmber"+phoneNumber);
            var temp_playerName = $("#PlayerName").val();
             
            if(temp_playerName.length > 0)
            {
                 console.log("before >>>player name"+playername +">>. temp_payername"+temp_playerName);
            playername = temp_playerName;
                console.log("After >>>player name"+playername +">>. temp_payername"+temp_playerName);
                //if new player send json to register the player with db..
             if(!oldplayer)
                        {
                            var promise = $.get(url + "/api/gamePlay/register",{uid:playerID,name:temp_playerName,phoneNumber:phoneNumber});
                            promise.done(function (data) {
                            console.log(">> NEW PLAYER ADDED WITH UID"+playername +">>> IME NO"+playerID);
                           playername = temp_playerName;
                            
                            }); 
                             promise.fail(function () {
                            console.log(">> NEW PLAYER creation failed");
                            }); 
                            
                            
                            registerGCMServer();
                        }     
               
            }
            else
            {
             
                 $("#PlayerName").attr("placeholder","We need your name ! please enter it");
                  console.log(">>>playerName inside iff loop button"+playername);   
              
            }
            
            
            // gcm >>

            // gcm >>
            
            console.log(">>>playerName inside submit button"+playername);
            // console.log("hoiiiqqiqiiqiqqiqiqiiqiqqiiq");
            $.mobile.navigate("#GameList",{transition:"flip",rel:"back"});
            
        });
    }
    if ("newGame" == $ui.toPage.attr("id"))
    {
        $("#GameName").val("");
        $("#GameEnterButton").on("click", function () {
            var text = $("#GameName").val();
            var promise = $.getJSON(url + "/api/gameService/newgame", {"gameName": text});
            promise.done(function (data) {

                console.log(111);
                if (data.Name == text)
                {

                }
                $.mobile.navigate("#GameList",{transition:"flip",rel:"back"});

            });



        });

    }




    //// Game Screeen logic
    if ("gamescreen" == $ui.toPage.attr("id"))
    {
        /*      $('.flip').hover(function(){
         $(this).find('.card').addClass('flipped').mouseleave(function(){
         $(this).removeClass('flipped');
         });
         return false;
         });*/

        $("imgbtn").on("swipe", function () {

            $(this).find('.card').addClass('flipped');
        });

//
        //Submit button logic
        $("#submit").on("click", function () {
//send data		
            console.log(btnpressed);
            console.log("button pressed");

            //if loop started....
            if(numberofTries<10)
            
            {
                            //      $(".card").toggleClass("flipped");
                            // clear the array...
                            btnonetwo =[];
                            btnpressed.forEach(function (entry)
                            {
                                var $button = $("#" + entry);
                                //  console.log($button);
                                //  console.log($(this));  
                                var cardClassName = $button.children($(".card")).attr("class");
                            var btnonetwo1 = $button.children($(".card")).attr("id");
                                var classSplitArray = cardClassName.split(" ");
                                var spl = classSplitArray[1].slice(0, 4);
                                var imgname = classSplitArray[1].slice(4, (classSplitArray[1].length));
                            btnonetwo.push(btnonetwo1)
                                console.log("spl" + spl);
                                console.log("imagename" + imgname);
                                console.log("bala >>>");
                                console.log("bala >>>" +btnonetwo1);

                                if (spl == "deck")
                                {
                                    var imgname2 = classSplitArray[1].slice(4, (classSplitArray[1].length));
                                    uniqImg.push(imgname2);
                                }
                                else
                                {
                                    uniqImg.push(imgname);
                                }

                                console.log(uniqImg);

                                // console.log(x);
                            });
                    //bala123

                            var promise = $.getJSON(url + "/gamesubmit", {"uid": playerID, "gameName": Currentgame, "pos1": uniqImg[0], "pos2": uniqImg[1], "card1": btnonetwo[0], "card2": btnonetwo[1]});
                            promise.fail(function () {

                                console.log("submit request failed");
                                      
                                console.log(" Submit Success numberofTries>>>> "+ numberofTries +">>>");

                            });
                            promise.done(function (message) {

                                
                                                    console.log("numberofTries" + numberofTries);

                                //""
               
                        
                                      console.log(" Submit Failed numberofTries>>>> "+ numberofTries +">>>");
                                console.log("JSON retiervd" );
                                console.log(message.data);
                                console.log("message from server" + message.result);
                                // flush uniqimage array
                                uniqImg=[];


                                if (message.result == "notok")
                                {
                                    console.log("inside");
                                    console.log(message.pos1);
                                    console.log(message.pos2);

                                    //bala
                                Flip180onSubmitbuttonFail();
                                reset();
                                    //comment start here,,
                /*


                                    //        $(".deck"+message.pos1).removeAttr("style");
                                    //    $(".deck"+message.pos2).removeAttr("style");
                                    $(".deck" + message.pos1).parents($(".imgbtn")).removeAttr("style");
                                    $(".deck" + message.pos2).parents($(".imgbtn")).removeAttr("style");
                                    //       $(".deck"+message.pos1).removeClass("flipped");
                                    //$(".deck"+message.pos2).removeClass("flipped");
                                    // $(".deck"+message.pos1).toggleClass("flippedfull");
                                    //    $(".deck"+message.pos2)
                                    //   $(".deck"+message.pos1).toggleClass("fullflip");
                                    //  $(".deck"+message.pos2).toggleClass("fullflip");
                                 //if card not equal flip 360
                                 var $imgbtn1=  $(".deck" + message.pos1).parent($(".imgbtn"));
                                 var $imgbtn2=  $(".deck" + message.pos2).parent($(".imgbtn"));
                                    //onfailfullrotate($imgbtn1,message.pos1);
                                    //onfailfullrotate($imgbtn2,message.pos2);

                                    console.log($imgbtn1.attr("id")+">>id");
                                    console.log($imgbtn2.attr("id")+">>id2");
                                    var imgId1=$imgbtn1.attr("id")
                                    var imgId2=$imgbtn2.attr("id");
                               var $btn1CardDiv1=  $("#"+imgId1).children(".deck"+message.pos1);
                                     var btn1CardclassName=  $btn1CardDiv1.attr("class");
                                // need to check weathwe flipped full is already there
                                     var str1 = btn1CardclassName;
                                    var str2 = "flippedfull";
                                    if(str1.indexOf(str2) != -1){

                                       btn1CardclassName = btn1CardclassName.slice(0,str1.indexOf(str2));
                                         alert(str2 + " found  >>> "+  btn1CardclassName.slice(0,str1.indexOf(str2))+" >>> " +btn1CardclassName);
                                    }

                                    var $btn1CardDiv2=  $("#"+imgId2).children(".deck"+message.pos2);
                                    console.log("btn2"+ $btn1CardDiv2.get());
                                                        console.log("btn2"+ $btn1CardDiv2.get());

                                    var btn2CardclassName=  $btn1CardDiv2.attr("class");
                                    console.log(btn1CardclassName);
                                    console.log(btn2CardclassName);
                                     $btn1CardDiv1.removeClass("flipped");
                                     $btn1CardDiv2.removeClass("flipped");

                                    $btn1CardDiv1.attr("class", btn1CardclassName+" flippedfull");
                                     $btn1CardDiv2.attr("class", btn2CardclassName+" flippedfull")
                                    $("#"+message.pos1+imgId1).removeClass("flipped");
                                    $("#"+message.pos2+imgId2).removeClass("flipped");
                                     console.log(message.pos1+imgId1);
                                    console.log(message.pos2+imgId2);
                                    $("#"+message.pos1+imgId1).attr("class", btn1CardclassName+" flippedfull");
                                    $("#"+message.pos2+imgId1).attr("class", btn2CardclassName+" flippedfull");

                                    var classname = $(".deck" + message.pos1).attr("class");
                                          classname=classname + " flippedfull";

                                    console.log(classname + "classes");
                */


                                       //comment end here,,

                                        // $(".deck"+message.pos1).attr("class",classname);


                                    /*$(".deck" + message.pos1).toggleClass("flippedfull");
                                    $(".deck" + message.pos2).toggleClass("flippedfull");*/
                                }
                                else
                                {
                                    console.log("Player" +playername + "scored a point ");
                                }

                            });

                            // $(".card").toggleClass("flipped");

                        }
             // if loop ends
            
            //else starts here...
            
                else{
                // show the pop up...
                    console.log("numberofTries" + numberofTries);
                console.log("come in else loop of timer of tires check up");
                    $("#Triespopup").popup( "open",{
                    positionTo:"window", transition:"flip"});
                    }
            console.log("num tries befr"+numberofTries);
            if(numberofTries<10){numberofTries++;}
               // numberofTries = (numberofTries<10) ? (numberofTries++) : numberofTries;
                    // numberofTries++;
             console.log("num tries aftr"+numberofTries);
                     $("#numberoftries").text(numberofTries+"/ 10");
            }

                       
                       
       );
         
        $("#backbutton").on("click", function(){
        $.mobile.navigate("#GameList",{transition:"flip",rel:"back"});
        });
        
        $("#reset").on("click", function () {

            if ($(".imgbtn").prop("disabled"))
            {
                console.log($(".imgbtn").prop("disabled"));
                $(".imgbtn").not(".winned").on("click", imgbtnOnclick);
                // $(".imgbtn").removeAttr("disabled");
                $(".imgbtn").prop("disabled", false);
                $(".imgbtn").removeAttr("style");
                $("#submit").attr("disabled", "");

            }
            imgCounter = 0;
            btnpressed = [];


        });

// all cards click event.....
        $(".imgbtn").not(".winned").on("click", imgbtnOnclick);
//  to select a contact and send the data to the freind to join the game
           $("#callFriend").on("click",function(){
        
        //contacts page add friend
            console.log("Insode contacts>>>>>>>>>>>>>>>>>");
            navigator.contacts.pickContact(function(contact){
                
                var arr = contact.phoneNumbers;
                console.log("phonenumbers");
                console.log(arr);
                console.log("Phone NUmber >>" )
                console.log(arr[0].value);
                
                toPHoneNumber=arr[0].value;
                
              
              var jsonObj = { "gamename": Currentgame };
               console.log("json objec created////>>>"+jsonObj);
                  console.log("json objec created////>>>"+JSON.stringify(jsonObj));
            //jsonObj ["gamename"] = Currentgame;
            console.log("fom sender"+Currentgame);
                console.log(Currentgame);
                console.log("Json Obj"+ jsonObj);
                
               $("#Friendmsg").text(defaultmsg);
  /*          $("#friendpopup").popup( "open",{
                    positionTo:"window", transition:"flip"});*/
               sendMessage(jsonObj);
                
        console.log('The following contact has been selected:' + JSON.stringify(contact));
    },function(err){
        console.log('Error: ' + err);
    });
         
        
        });



    }


    //button on click





});


function sendMessage(jsonObj)
{
var promise= $.post("http://10.10.2.170:8080/gcmpush/publish",{
name:playername,
phone:toPHoneNumber,
message: defaultmsg,
jsonData: JSON.stringify(jsonObj)
});
promise.done(function(){
console.log("suceess pushed data via CHUCK servver to GCM");
});
promise.fail(function(){
console.log("OOOOPs ! failed pushed data via CHUCK servver to GCM");
});
}



function onfailfullrotate($imgbtn,deckid)
{
    //to test//
    console.log("Id"+$imgbtn.attr("id"));
    //end test//

    
     var imgId= $imgbtn.attr("id");
    $btncardDiv=$("#"+imgId).children(".card");
    var btnclassname=$btncardDiv.attr("class");
    
    $("#"+deckid+imgId).removeClass("flipped");
    $("#"+deckid+imgId).attr("class", btnclassname+" flippedfull");
    
  
    
    $btncardDiv.removeClass("flippedfull"); 
    
}

function Flip360onSubmitbuttonFail(){
    for(var a=0 ; a<btnonetwo.length;a++ ){
         $("#"+btnonetwo[a]).removeClass("flipped");
        $("#"+btnonetwo[a]).removeClass("flippedfull");
        var cn = $("#"+btnonetwo[a]).attr("class" );
        $("#"+btnonetwo[a]).attr("class",cn+" flippedfull");
    }
    setTimeout(function() { $("#"+btnonetwo[0]).removeClass("flippedfull");$("#"+btnonetwo[1]).removeClass("flippedfull");}, 2000);
}

function Flip180onSubmitbuttonFail(){
    for(var a=0 ; a<btnonetwo.length;a++ ){
         $("#"+btnonetwo[a]).removeClass("flipped");
        $("#"+btnonetwo[a]).removeClass("flippedfull");
        var cn = $("#"+btnonetwo[a]).attr("class" );
        $("#"+btnonetwo[a]).attr("class",cn+" flipped");
        // remove the style green.
        $("#"+btnonetwo[a]).parent().removeAttr("style");
        
    }
    setTimeout(function() {
        console.log("---> calling set timeout");
        $("#"+btnonetwo[0]).removeClass("flipped");$("#"+btnonetwo[1]).removeClass("flipped");}, 2000);
}

function reset (){
            if ($(".imgbtn").prop("disabled"))
            {
                console.log($(".imgbtn").prop("disabled"));
                // stop adding event handlers to already passed cards,,,
                $(".imgbtn").not(".winned").on("click", imgbtnOnclick);
               // $(".imgbtn").on("click", imgbtnOnclick);
                // $(".imgbtn").removeAttr("disabled");
                $(".imgbtn").prop("disabled", false);
                $(".imgbtn").removeAttr("style");
                $("#submit").attr("disabled", "");

            }
            imgCounter = 0;
            btnpressed = [];
}

     function imgbtnOnclick() {

            //$(this).toggleClass("ongreen");
            //$(this).addClass("ongreen");

            // $("this").css("background-color", "yellow");
            if ($(this).attr("style"))
            {
                $(this).removeAttr("style");
                imgCounter--;
                var i = btnpressed.indexOf($(this).attr("id"));
                if (i > -1)
                {
                    btnpressed.splice(i, 1);
                }
                //
            }
            else
            {
                imgCounter++;
                $(this).attr("style", "background-color:green;");
                btnpressed.push($(this).attr("id"));

            }
            console.log($(this).attr("id"));

            console.log(imgCounter);
            if (imgCounter == 2)
            {
                $(".imgbtn").prop("disabled", true).off('click');
                $("#submit").removeAttr("disabled");
                $("#reset").removeAttr("disabled");
            }
            if (imgCounter == 1)
            {
                $("#reset").attr("disabled", "");
            }
        }

