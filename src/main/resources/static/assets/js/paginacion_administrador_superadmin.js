$(document).ready(function() {
    function showTableSegment(table, startIndex, endIndex) {
        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $('#prev-btn1').prop('disabled', startIndex === 0);
        $('#next-btn1').prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5 + 1;
        $('#page-num1').text(currentPage);
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

        $('#prev-btn1').click(function() {
            updateIndex(false);
        });

        $('#next-btn1').click(function() {
            updateIndex(true);
        });
        $('#AdministradorTable').css('display', '');
        /*    $('#superadmin-table').css('display', '');
            $('#pagination').css('display', '');*/

    }

    setupTablePagination($('#superadmin_administrador_table'), 5);

    var firstName;
    var lastName;
    var id1;

    $('#staticBackdrop').on('show.bs.modal', function(event) {
        var button = event.relatedTarget;
        var $row = $(button).closest('tr') // Asegúrate de buscar solo filas visibles
        id1 = $row.find('.row-id').text();
        firstName = $row.find('td:nth-child(2)').text();
        lastName = $row.find('td:nth-child(3)').text();
        console.log(`firstName: ${firstName}, lastName: ${lastName}`);
        console.log(`ID: ${id1}`);
        $('#staticBackdropLabel').html(`Iniciar sesión`);
        $('.modal-body').html(`¿Loguearse como ${firstName} ubicado en la fila ${id1}?` );
    });
    /*
    setTimeout(function() {
        $('#superadmin-table').css('display', '');
        $('#pagination').css('display', '');
    }, 0);
     */
});
