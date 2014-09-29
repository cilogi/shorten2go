(function () {
    $(document).ready(function() {
        $("#input").keypress(function (e) {
            if (e.which == 13) {
                $("#spinner").addClass("csspinner");
                var val = encodeURIComponent($(this).val());
                $.ajax({
                    type: "GET",
                    url: "/shorten?url="+val,
                    dataType: "text",
                    success: function (data) {
                        var ref = "<a href='"+data+"'>"+data+"</a>" ;
                        $("#answer").html(ref);
                        $("#answer-group").show();
                        this.blur();
                    }.bind(this),
                    error: function(jqXHR, status, error) {
                        $("#answer-group").show();
                        $("#answer").html(status + ": " + error);
                        this.blur();
                    }.bind(this),
                    complete: function() {
                        $("#spinner").removeClass("csspinner");
                    }

                });
            }
        });
    });
})();



