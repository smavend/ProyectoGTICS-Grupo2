$(document).ready(function() {
    function showTableSegment(table, startIndex, endIndex) {
        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $('#prev-btn5').prop('disabled', startIndex === 0);
        $('#next-btn5').prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5+ 1;
        $('#page-num5').text(currentPage);
    }

    function setupTablePagination(table, itemsPerPage) {
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

        $('#prev-btn5').click(function() {
            updateIndex(false);
        });

        $('#next-btn5').click(function() {
            updateIndex(true);
        });
        $('#citasTable').css('display', '');
        /*    $('#superadmin-table').css('display', '');
            $('#pagination').css('display', '');*/

    }

    setupTablePagination($('#Doctores_Citas_table'), 5);


    /*
    setTimeout(function() {
        $('#superadmin-table').css('display', '');
        $('#pagination').css('display', '');
    }, 0);
     */
});