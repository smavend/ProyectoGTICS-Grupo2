$(document).ready(function() {
    function showTableSegment(table, startIndex, endIndex) {
        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $('#prev-btn7').prop('disabled', startIndex === 0);
        $('#next-btn7').prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5 + 1;
        $('#page-num7').text(currentPage);
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

        $('#prev-btn7').click(function() {
            updateIndex(false);
        });

        $('#next-btn7').click(function() {
            updateIndex(true);
        });
        $('#RecibosTable').css('display', '');
        /*    $('#superadmin-table').css('display', '');
            $('#pagination').css('display', '');*/

    }

    setupTablePagination($('#Doctores_Recibos_table'), 5);


    /*
    setTimeout(function() {
        $('#superadmin-table').css('display', '');
        $('#pagination').css('display', '');
    }, 0);
     */
});