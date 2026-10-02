import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import MaterialTable from 'material-table';
import {
    Button,
    Icon,
    IconButton,
    TextField,
    TablePagination,
    Grid,
    FormControl,
    InputLabel,
    Select,
    MenuItem
} from '@material-ui/core';
import TaskDialog from './TaskDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function TaskIndex() {
    const { taskStore } = useStore();
    const {
        taskList,
        totalElements,
        page,
        pageSize,
        priority,
        status,
        setPage,
        setPageSize,
        setKeyword,
        setPriorityFilter,
        setStatusFilter,
        updatePageData,
        handleAddItem,
        handleEditItem,
        handleDeleteItem,
        handleExportExcel,
        shouldOpenDialog,
        handleCloseDialog,
        shouldOpenConfirmationDialog,
        handleCloseConfirmationDialog,
        handleConfirmDelete
    } = taskStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const getPriorityName = (val) => {
        if (val === 1) return 'Cao';
        if (val === 2) return 'Trung bình';
        if (val === 3) return 'Thấp';
        return '';
    };

    const getStatusName = (val) => {
        if (val === 1) return 'Mới tạo';
        if (val === 2) return 'Đang làm';
        if (val === 3) return 'Hoàn thành';
        if (val === 4) return 'Tạm hoãn';
        return '';
    };

    const columns = [
        { title: 'Tên công việc', field: 'name' },
        { title: 'Dự án', field: 'projectName' },
        { title: 'Người thực hiện', field: 'staffName' },
        {
            title: 'Mức độ ưu tiên',
            field: 'priority',
            render: (rowData) => getPriorityName(rowData.priority)
        },
        {
            title: 'Trạng thái',
            field: 'status',
            render: (rowData) => getStatusName(rowData.status)
        },
        { title: 'Mô tả', field: 'description' },
        {
            title: 'Hành động',
            render: (rowData) => (
                <div>
                    <IconButton color="primary" onClick={() => handleEditItem(rowData)}>
                        <Icon>edit</Icon>
                    </IconButton>
                    <IconButton color="secondary" onClick={() => handleDeleteItem(rowData.id)}>
                        <Icon>delete</Icon>
                    </IconButton>
                </div>
            )
        }
    ];

    const handleChangePage = (event, newPage) => {
        setPage(newPage + 1);
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(parseInt(event.target.value, 10));
    };

    return (
        <div className="m-sm-30">
            <div className="mb-sm-30">
                <h2>Quản lý Công việc (Task)</h2>
            </div>
            <Grid container spacing={2} alignItems="center">
                <Grid item xs={12} sm={3}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleAddItem}
                        startIcon={<Icon>add</Icon>}
                        style={{ marginRight: 8 }}
                    >
                        Thêm mới
                    </Button>
                    <Button
                        variant="contained"
                        color="default"
                        onClick={handleExportExcel}
                        startIcon={<Icon>cloud_download</Icon>}
                    >
                        Xuất Excel
                    </Button>
                </Grid>
                <Grid item xs={12} sm={3}>
                    <TextField
                        variant="outlined"
                        size="small"
                        fullWidth
                        placeholder="Tìm kiếm công việc..."
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                </Grid>
                <Grid item xs={12} sm={3}>
                    <FormControl fullWidth variant="outlined" size="small">
                        <InputLabel id="filter-priority-label">Mức độ ưu tiên</InputLabel>
                        <Select
                            labelId="filter-priority-label"
                            label="Mức độ ưu tiên"
                            value={priority !== null ? priority : ''}
                            onChange={(e) => setPriorityFilter(e.target.value !== '' ? e.target.value : null)}
                        >
                            <MenuItem value="">Tất cả mức độ</MenuItem>
                            <MenuItem value={1}>Cao</MenuItem>
                            <MenuItem value={2}>Trung bình</MenuItem>
                            <MenuItem value={3}>Thấp</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item xs={12} sm={3}>
                    <FormControl fullWidth variant="outlined" size="small">
                        <InputLabel id="filter-status-label">Trạng thái</InputLabel>
                        <Select
                            labelId="filter-status-label"
                            label="Trạng thái"
                            value={status !== null ? status : ''}
                            onChange={(e) => setStatusFilter(e.target.value !== '' ? e.target.value : null)}
                        >
                            <MenuItem value="">Tất cả trạng thái</MenuItem>
                            <MenuItem value={1}>Mới tạo</MenuItem>
                            <MenuItem value={2}>Đang làm</MenuItem>
                            <MenuItem value={3}>Hoàn thành</MenuItem>
                            <MenuItem value={4}>Tạm hoãn</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
            </Grid>
            <div className="mt-16">
                <MaterialTable
                    title=""
                    data={taskList}
                    columns={columns}
                    options={{
                        selection: false,
                        actionsColumnIndex: -1,
                        paging: false,
                        search: false,
                        toolbar: false
                    }}
                    localization={{
                        body: {
                            emptyDataSourceMessage: "Không có dữ liệu"
                        }
                    }}
                />
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={totalElements}
                    rowsPerPage={pageSize}
                    page={page - 1}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                    labelRowsPerPage="Số hàng mỗi trang:"
                />
            </div>
            {shouldOpenDialog && (
                <TaskDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}
            <GlobitsConfirmationDialog
                open={shouldOpenConfirmationDialog}
                onConfirmDialogClose={handleCloseConfirmationDialog}
                onYesClick={handleConfirmDelete}
                title="Xác nhận xóa"
                text="Bạn có chắc chắn muốn xóa công việc này không?"
                agree="Xóa"
                cancel="Hủy"
            />
        </div>
    );
});
