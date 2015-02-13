$(function ()
{


    getAllGameInstance();
    var src = new EventSource("api/gameService/dynamicRequest");
    src.onmessage = messageHandler;
    $("#btnCreateGame").on("click",
            function () {
                // clear all the result
                $("#list").replaceWith($("<li>")
                        .attr("id", "list")
                        );
                var promise = $.getJSON("api/gameService/game",
                        {"gameName": $("#inpt_gamename").val()});
                promise.done(function (data) {
                    for (var i = 0, len = data.length; i < len; i++) {
                        console.log(data[i]);
                        $("#list").append($("<li>")
                                .text(data[i].Name)
                                .on("click", function () {

                                }));

                    }

                });
                $("#inpt_gamename").val("");
            });


});

function messageHandler(message) {
    console.log("Test");
    console.log(message.data);
    $("#list").append($("<li>")
            .text(message.data));
}

function getAllGameInstance() {
    var promise = $.getJSON("api/gameService/");
    promise.done(function (data) {
        for (var i = 0, len = data.length; i < len; i++) {
            console.log(data[i]);
            $("#list").append($("<li>")
                    .text(data[i].Name)
                    );

        }
    });
}
