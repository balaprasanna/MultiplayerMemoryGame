
//var xa =($(".imgbtn").length);
//console.log(x);
// 
var btnpressed = [];
var uniqImg=[];
var imgCounter = 0;
var playername = "";
var Currentgame = ""
var gameSse;
var Playerjson = {"gameSequence": ["1", "2", "3", "4", "7", "8", "9", "10", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "2", "3", "4", "5"]};

//var url ="http://10.10.1.149:8080/javacatest-master";
//var url = "http://192.168.1.13:8080/javacatest-master";
var url = "http://localhost:8080/javacatest-master";



$(document).on("pagecontainerbeforeshow", function (_, $ui)
{
    //

    if ("GameList" == $ui.toPage.attr("id")) {
        console.log(playername);


        $("#usernametxt").text(playername);
        //$("#listgames").empty();

        var promise = $.getJSON(url + "/api/gameService/getGames");
        promise.done(function (data) {
            for (var i = 0, len = data.length; i < len; i++) {
                $("#listgames").append(createListItem(i, data[i].Name));
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


    {  
        $(".card").toggleClass("flipped");
        

        var ready = false;
        //sSe broadcaster is already created
        gameSse = new EventSource(url + "/api/gamePlay/" + Currentgame + "/" + playername);
        //    gameSse.onmessage = messageHandler;
        gameSse.addEventListener(Currentgame,messageHandler);
        //flip try

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


        function messageHandler(message)
         {
         ready=true;
         
         Playerjson=JSON.parse(message.data);
         console.log(Playerjson);
         console.log("Inside SEE first sent mesage "+ Playerjson.gameSequence[0]);
         
         $(".flip").addClass('flipped');
         Playerjson.gameSequence.forEach(function(enrty){
         //  console.log(enrty);
         });
         
         //console.log("helllllooopoo"+json.sequence[2])
         $('.imgbtn').empty();
         $("#submit").attr("disabled","");
         $("#reset").attr("disabled","");
         $('.imgbtn').each(function(i, obj) {
         
         //console.log(i);
         //console.log($(this));
        // picId=i;
       //  picId++;
         $(this).attr({"class":"ui-btn imgbtn "});
         // console.log("oicid>>"+picId);
         // console.log(">>>" + Playerjson.gameSequence[picId]);
         //$(this).append($("<div>").attr("class","back"));
         $(this).append(createImg(Playerjson.gameSequence[i]));
         });
         
         
        Playerjson.WinningSequence.forEach(function(entry){
          console.log("Winning items"+ entry);
          $(".deck"+entry).toggleClass("flipped");
        
        });
             
        $(".card").toggleClass("flipped");
             
         
         
         }




/*

                                 <div class="card" >
                            <figure class="front"></figure>
                        <figure class="back"></figure>
                                  </div>*/




        function createImg(name) {
            var $front=$("<figure>").attr("class","front");
            var $back=$("<figure>").attr("class","back");
           var $img = $("<img>").attr({"src": "ImagesCards/" + name + ".png", "style": "height:200px;width:150px;"});
//            var $img = $("<img>").attr({"src": "ImagesCards/" + name + ".png"});
            var $div=$("<div>").attr("class","card "+"deck"+name);
            
              $front.append($img);
            $div.append($front);
            $div.append($back);
            //            var $img = $("<img>").attr({"src": "ImagesCards/" + name + ".png", "style": "height:100px;width:150px;"});
            
            return ($div);
        }

    }
    //new Game page
    if ("newGame" == $ui.toPage.attr("id"))
    {

        $("GameName").val("");
    }






});


$(document).on("pagecontainershow", function (_, $ui)
{



    // game list page 
    if ("GameList" == $ui.toPage.attr("id")) {



        $('#listgames').on('click', 'li a', function () {
            //    alert($(this).text()+ "this"); // id of clicked li by directly accessing DOMElement property
            Currentgame = $(this).text();
            $.mobile.navigate("#gamescreen");
        });



        $("#NewGameButton").on("click", function () {
            $.mobile.navigate("#newGame");
        });
    }

    if ("newPlayer" == $ui.toPage.attr("id")) {
        $("#startButton").on("click", function () {

            playername = $("#PlayerName").val();
            console.log(playername);
            // console.log("hoiiiqqiqiiqiqqiqiqiiqiqqiiq");
            $.mobile.navigate("#GameList");

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
                $.mobile.navigate("#GameList");

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

        
        //Submit button logic
        $("#submit").on("click", function () {
//send data		
            console.log(btnpressed);
            console.log("button pressed");
            
            
            
            btnpressed.forEach(function(entry)
                           { 
                var $button=$("#"+entry);
              //  console.log($button);
              //  console.log($(this));  
          var cardClassName=  $button.children($(".card")).attr("class");
                var classSplitArray=cardClassName.split(" ");
                var spl=classSplitArray[1].slice(0,4);
                var imgname=classSplitArray[1].slice(4,(classSplitArray[1].length));
                
                console.log("spl"+spl);
                console.log("imagename"+imgname);
                
                if(spl=="deck")
                {
                     var imgname2=classSplitArray[1].slice(4,(classSplitArray[1].length));
                    uniqImg.push(imgname2);
                }
                else 
                {
                    uniqImg.push(imgname);
                }
            
                console.log(uniqImg);
                
               // console.log(x);
            });
            
            
             var promise= $.getJSON(url+"/gamesubmit", {"uid":playername,"gameName":Currentgame,"pos1":uniqImg[0],"pos2":uniqImg[1]});
            promise.fail(function(){
            
            console.log("submit request failed");
            });
            promise.done(function(message){
            console.log("JSON retiervd"+message);
                if("resultObject"==message.Id){
                
                }
            
            });
                         
           // $(".card").toggleClass("flipped");

        });

        $("#reset").on("click", function () {

            if ($(".imgbtn").prop("disabled"))
            {
                console.log($(".imgbtn").prop("disabled"));
                $(".imgbtn").on("click",imgbtnOnclick);
               // $(".imgbtn").removeAttr("disabled");
                $(".imgbtn").prop("disabled",false);
                $(".imgbtn").removeAttr("style");
                $("#submit").attr("disabled", "");

            }
            imgCounter = 0;
            btnpressed = [];


        });


        $(".imgbtn").on("click",imgbtnOnclick);
                        
                         function imgbtnOnclick () {

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
                $(".imgbtn").prop("disabled",true).off('click');
                $("#submit").removeAttr("disabled");
                $("#reset").removeAttr("disabled");
            }
            if (imgCounter == 1)
            {
                $("#reset").attr("disabled", "");
            }
        }



    }


    //button on click





});