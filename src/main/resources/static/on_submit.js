$(document).ready(function() {
    $("form").submit(function(event) {
        hideAll()

        var body = JSON.stringify({
            personalCode: $("#personalCode").val(),
            amount: $("#amount").val(),
            period: $("#period").val(),
        })

        $.ajax({
            type: "POST",
            url: "/loan",
            data: body,
            contentType: 'application/json; charset=utf-8'
        }).done(function(data) {
            if (data.decision) {
                $("#response_accepted").show()
                if (data.maxSum) {
                    $("#response_amount").show()
                    $("#response_amount").html("Maximum possible loan: " + data.maxSum + "$")
                }
            } else {
                $("#response_denied").show()
                if (data.proposedPeriod) {
                    $("#response_period").show()
                    $("#response_period").html("Minimum possible period: " + data.proposedPeriod + " months")
                }
                if (data.maxSum) {
                    $("#response_amount").show()
                    $("#response_amount").html("Maximum possible loan: " + data.maxSum + "$")
                }
            }
        }).fail(function(data) {
            $("#alert").show()
            $("#alert").html(data.responseJSON.errorMessage)
        });;

        event.preventDefault();
    });
});

function hideAll() {
    $("#alert").hide()
    $("#alert").html("")
    $("#success").hide()
    $("#success").html("")
    $("#response_accepted").hide()
    $("#response_denied").hide()
    $("#response_amount").hide()
    $("#response_amount").html("")
    $("#response_period").hide()
    $("#response_period").html("")
}