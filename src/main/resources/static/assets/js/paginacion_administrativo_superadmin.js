$(document).ready(function() {
    function showTableSegment(table, startIndex, endIndex) {
        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $('#prev-btn2').prop('disabled', startIndex === 0);
        $('#next-btn2').prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5 + 1;
        $('#page-num2').text(currentPage);
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

        $('#prev-btn2').click(function() {
            updateIndex(false);
        });

        $('#next-btn2').click(function() {
            updateIndex(true);
        });
        $('#AdministrativoTable').css('display', '');
        /*    $('#superadmin-table').css('display', '');
            $('#pagination').css('display', '');*/

    }

    setupTablePagination($('#superadmin_administrativos_table'), 5);

    $('#staticBackdrop').on('show.bs.modal', function(event) {
        var button = event.relatedTarget;
        var $row = $(button).closest('tr'); // Asegúrate de buscar solo filas visibles
        var id1 = $row.find('.row-id').text();
        var firstName = $row.find('td:nth-child(3)').text();
        var apellidos = $row.find('td:nth-child(4)').text();
        var primerApellido = apellidos.split(' ')[0];
        var lastName = `${primerApellido}`;
        console.log(`firstName: ${firstName}, lastName: ${lastName}`);
        console.log(`ID: ${id1}`);
        $('#staticBackdropLabel').html(`Iniciar sesión`);
        $('.modal-body').html(`¿Loguearse como ${firstName} ${lastName}?`);
    });
    /*
    setTimeout(function() {
        $('#superadmin-table').css('display', '');
        $('#pagination').css('display', '');
    }, 0);
     */
});
