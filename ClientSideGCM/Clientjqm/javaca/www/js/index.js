$(function (){
    var source= null;
  
  $("#btnjoin").on("click",function (){
     var gameName = $("#inpt_gamename").val(); 
   
   if(!source){
     source = new EventSource("api/gameService/"+gameName);  
     source.onmessage = messageHandler;
    }
    // check for exit request...
    if(gameName=="exit")
    {
//       var promise = $.get("/api/gameService/exit");
//       promise.done(function (){
//           console.log("exit finished");
//       });
        source.close();
        source = null;
    }
  
  }); 
  
  $("#btnKill").on("click",function (){
     var src = new EventSource("api/gameService/exit");
     src.onmessage = messageHandler;
  });
});

 function mhandler() {
    var t = $("#chatArea").text();
    $("#chatArea").text(message.data + "\n" + t);
    
}

 function messageHandler(message) {
    var t = $("#chatArea").text();
    $("#chatArea").text(message.data + "\n" + t);
    
}

