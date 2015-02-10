$(function () {
    
    var url = "http://10.10.1.149:8080/javacatest-master";

            var promsise = $.getJSON(url+"/api/CommonView/getRanklist");
            promsise.done(function (data){
                i=0;
$.each(data,function(k,v){
i++;
//console.log("Key"+k+"  " +"Value"+v);
$("#ranklist").append(createTr(i,k,v));

});    
            }
                         );
    
    
    promsise.fail(function(){
    
    console.log("Rank List getJson Failed");
    });
    

                
    
    
    
    function createTr(i,playername,playerscore){    
        var $tr=$("<tr>") ;
        var $td3=$("<td>").text(i);
        var $td1=$("<td>").text(playername);
        var $td2=$("<td>").text(playerscore);
        $tr.append($td3);
         $tr.append($td1);
        $tr.append($td2);
       
          console.log($tr);
        return $tr;
      
    }
    

            });