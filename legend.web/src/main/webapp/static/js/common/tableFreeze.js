window.Components = window.Components || {};
$.extend(window.Components, {
    freezeTable: function (table) {
        var freezeRowNum = +table.attr('freezeRowNum') || 0;
        var freezeColumnNum = +table.attr('freezeColumnNum');
        var width = 823 - 5,
            height = table[0].clientHeight + 20,
            html = '';
        var tableId = table.attr('id');
        var divTableLayout = $("#" + tableId + "_tableLayout");
        var divTableData = null;

        if(!tableId) {
            console.error('need table id');
        }
        if (divTableLayout.length != 0) {
            divTableLayout.find('.table-clone').remove();

            divTableData = divTableLayout.find('.table-data');
        } else {
            table.after("<div id='" + tableId + "_tableLayout' style='overflow:hidden;height:" + height + "px; width:" + width + "px;'></div>");

            divTableLayout = $("#" + tableId + "_tableLayout");

            if (freezeColumnNum > 0)
                html += '<div id="' + tableId + '_tableColumn" class="table-column" style="padding: 0px;"></div>';

            html += '<div id="' + tableId + '_tableData" class="table-data" style="padding: 0px;"></div>';
            $(html).appendTo(divTableLayout);

            divTableData = $("#" + tableId + "_tableData").append(table);
        }

        var divTableColumn = freezeColumnNum > 0 ? $("#" + tableId + "_tableColumn") : null;

        var tableColumnClone = table.clone(true);

        tableColumnClone.addClass('table-clone')
            .attr("id", tableId + "_tableColumnClone")
            .find('*[id]').each(function () {
                $(this).removeAttr('id');
            })
        divTableColumn.append(tableColumnClone);

        $("#" + tableId + "_tableLayout table").css("margin", "0");

        if (freezeColumnNum > 0) {
            var ColumnsWidth = 0;
            var ColumnsNumber = 0;

            divTableColumn.find("tr:eq(" + freezeRowNum + ")").find("td:lt(" + freezeColumnNum + "), th:lt(" + freezeColumnNum + ")").each(function () {
                if (ColumnsNumber >= freezeColumnNum)
                    return;

                ColumnsWidth += $(this).outerWidth(true);

                ColumnsNumber += $(this).attr('colSpan') ? parseInt($(this).attr('colSpan')) : 1;
            });
            ColumnsWidth += 2;

            divTableColumn.css("width", ColumnsWidth);
        }

        if(divTableColumn.length) {
            divTableColumn.css({
                "overflow": "hidden",
                "height": height - 17,
                "position": "absolute",
                "z-index": "40"
            });
        }
        divTableData.css({"overflow-x": "scroll", "width": width, "height": height, "position": "absolute"});

        divTableLayout.css('height', height);

        divTableColumn != null && divTableColumn.offset(divTableLayout.offset());
        divTableData.offset(divTableLayout.offset());
    }
});

