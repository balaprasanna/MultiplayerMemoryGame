$(function ()
{
    getAllGameInstance();
            $("#btnCreateGame").on("click",
            function (){
                // clear all the result
               $("#list").replaceWith($("<li>")
                                          .attr("id","list")
                                          );
               var promise = $.getJSON("api/gameService/game",
                            {"gameName":$("#inpt_gamename").val()});
               promise.done(function (data){
                  for (var i=0, len=data.length; i < len; i++) {
                  console.log(data[i]);
                     $("#list").append($("<li>")
                             .text(data[i].Name)
                             .on("click",function(){
                             }));  
               }
               });
            });
            
            
});

function getAllGameInstance(){
        var promise = $.getJSON("api/gameService/");
               promise.done(function (data){
                  for (var i=0, len=data.length; i < len; i++) {
                  console.log(data[i]);
                     $("#list").append($("<li>")
                             .text(data[i].Name)
                             );
                     
        }
        });
    }
