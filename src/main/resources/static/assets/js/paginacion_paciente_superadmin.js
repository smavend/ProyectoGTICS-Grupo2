$(document).ready(function() {
    function showTableSegment(table, startIndex, endIndex) {

        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $('#prev-btn4').prop('disabled', startIndex === 0);
        $('#next-btn4').prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5 + 1;
        $('#page-num4').text(currentPage);
    }

    // function addEmptyRows(table, itemsPerPage) {
    //     var numRows = table.find('tbody tr').length;
    //     var numEmptyRows = itemsPerPage - (numRows % itemsPerPage);
    //
    //     if (numEmptyRows === itemsPerPage) return;
    //
    //     for (var i = 0; i < numEmptyRows; i++) {
    //         var emptyRow = '<tr style="display:none">';
    //         for (var j = 0; j < table.find('thead th').length; j++) {
    //             emptyRow += '<td>&nbsp;</td>';
    //         }
    //         emptyRow += '</tr>';
    //         table.find('tbody').append(emptyRow);
    //     }
    // }

    function setupTablePagination(table, itemsPerPage) {
        // addEmptyRows(table, itemsPerPage);
        var startIndex = 0;
        var endIndex = itemsPerPage;

        showTableSegment(table, startIndex, endIndex);

        function updateIndex(isNext) {
            if (isNext) {
                if (startIndex + itemsPerPage < table.find('tbody tr').length) {
                    startIndex += itemsPerPage;
                }
            } else {
                if (startIndex > 0) {
                    startIndex -= itemsPerPage;
                }
            }
            endIndex = startIndex + itemsPerPage;
            showTableSegment(table, startIndex, endIndex);
        }

        $('#prev-btn4').click(function() {
            updateIndex(false);
        });

        $('#next-btn4').click(function() {
            updateIndex(true);
        });
        $('#PacientesTable').css('display', '');
        /*    $('#superadmin-table').css('display', '');
            $('#pagination').css('display', '');*/

    }

    setupTablePagination($('#superadmin_Pacientes_table'), 5);


    /*
    setTimeout(function() {
        $('#superadmin-table').css('display', '');
        $('#pagination').css('display', '');
    }, 0);
     */
});
