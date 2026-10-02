import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import GlobitsTable from 'app/common/GlobitsTable';
import GlobitsSearchInput from 'app/common/GlobitsSearchInput';
import TimeSheetDialog from './TimeSheetDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';
import { Button, Grid, Icon, IconButton, List, ListItem, ListItemText, Typography, Paper, Chip } from "@material-ui/core";
import moment from "moment";

export default observer(function TimeSheetIndex() {
    const { timeSheetStore } = useStore();
    const {
        itemList,
        totalElements,
        totalPages,
        page,
        pageSize,
        shouldOpenDialog,
        shouldOpenConfirmationDialog,
        projectList,
        selectedProjectId,
        setPage,
        setPageSize,
        setKeyword,
        setSelectedProjectId,
        handleEditItem,
        handleAddItem,
        handleCloseDialog,
        handleDeleteItem,
        handleConfirmDelete,
        handleCloseConfirmationDialog,
        updatePageData,
        fetchProjectList
    } = timeSheetStore;

    useEffect(() => {
        fetchProjectList();
        updatePageData();
    }, [updatePageData, fetchProjectList]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage)
    }

    const setRowsPerPage = (event) => {
        setPageSize(Number(event.target.value))
    }

    const handleSearch = (searchObject) => {
        setKeyword(searchObject.keyword);
    };

    const getPriorityLabel = (value) => {
        switch (value) {
            case 1: return <span className="badge bg-secondary p-2" style={{color: "gray"}}>Thấp</span>;
            case 2: return <span className="badge bg-info p-2" style={{color: "blue"}}>Trung bình</span>;
            case 3: return <span className="badge bg-warning p-2" style={{color: "orange"}}>Cao</span>;
            case 4: return <span className="badge bg-danger p-2" style={{color: "red"}}>Cấp bách</span>;
            default: return "";
        }
    }

    const getPriorityText = (value) => {
        switch (value) {
            case 1: return "Thấp";
            case 2: return "Trung bình";
            case 3: return "Cao";
            case 4: return "Cấp bách";
            default: return "";
        }
    }

    const columns = [
        {
            title: "Thao tác", field: "custom", align: "center", width: "100",
            render: (rowData) => (
                <div className='flex flex-middle flex-center'>
                    <IconButton onClick={() => handleEditItem(rowData.id)}>
                        <Icon color="primary">edit</Icon>
                    </IconButton>
                    <IconButton onClick={() => handleDeleteItem(rowData.id)}>
                        <Icon color="error">delete</Icon>
                    </IconButton>
                </div>
            )
        },
        {
            title: "Công việc", field: "custom", align: "left", width: "250",
            render: (rowData) => {
                if(rowData.details && rowData.details.length > 0) {
                    return rowData.details.map(d => d.workingItemTitle).join(", ");
                }
                return rowData.description || "";
            }
        },
        {
            title: "Thời gian", field: "custom", align: "left", width: "300",
            render: (rowData) => {
                let startStr = rowData.startTime ? moment(rowData.startTime).format("HH:mm DD/MM/YYYY") : "";
                let endStr = rowData.endTime ? moment(rowData.endTime).format("HH:mm DD/MM/YYYY") : "";
                let totalStr = "";
                if (rowData.startTime && rowData.endTime) {
                    let duration = moment.duration(moment(rowData.endTime).diff(moment(rowData.startTime)));
                    let hours = duration.asHours();
                    totalStr = `${hours.toFixed(1)} tiếng`;
                }
                return (
                    <div>
                        {startStr && <div>Thời gian bắt đầu: {startStr}</div>}
                        {endStr && <div>Thời gian kết thúc: {endStr}</div>}
                        {totalStr && <div>Tổng thời gian: {totalStr}</div>}
                    </div>
                )
            }
        },
        {
            title: "Mức độ ưu tiên", field: "custom", align: "center", width: "150",
            render: (rowData) => {
                return (
                    <div style={{
                        padding: "4px 8px", 
                        backgroundColor: rowData.priority === 4 ? "#d32f2f" : "transparent",
                        color: rowData.priority === 4 ? "white" : "inherit",
                        borderRadius: "4px",
                        display: "inline-block"
                    }}>
                        {getPriorityText(rowData.priority)}
                    </div>
                )
            }
        },
        {
            title: "Người thực hiện", field: "custom", align: "left", width: "220",
            render: (rowData) => {
                const getStaffName = (staff) => {
                    if (!staff) return "";
                    return staff.displayName || 
                           `${staff.lastName || ""} ${staff.firstName || ""}`.trim() || 
                           `${staff.firstName || ""} ${staff.lastName || ""}`.trim() || 
                           staff.name || 
                           staff.staffCode || 
                           "";
                };

                let employees = [];

                // 1. Lấy từ danh sách nhân viên tham gia TimeSheet (timeSheetStaff)
                if (rowData.timeSheetStaff && Array.isArray(rowData.timeSheetStaff)) {
                    rowData.timeSheetStaff.forEach((s) => {
                        const name = getStaffName(s.staff || s);
                        if (name) employees.push(name);
                    });
                }

                // 2. Lấy từ chi tiết công việc (details)
                if (rowData.details && Array.isArray(rowData.details)) {
                    rowData.details.forEach((d) => {
                        const name = getStaffName(d.employee);
                        if (name) employees.push(name);
                    });
                }

                // Loại bỏ trùng lặp
                employees = [...new Set(employees)];

                if (employees.length > 0) {
                    return (
                        <div style={{ display: "flex", flexWrap: "wrap", gap: "4px" }}>
                            {employees.map((emp, index) => (
                                <Chip
                                    key={index}
                                    label={emp}
                                    size="small"
                                    color="primary"
                                    variant="outlined"
                                    style={{ fontSize: "12px", height: "24px" }}
                                />
                            ))}
                        </div>
                    );
                }
                return <span style={{ color: "#999", fontStyle: "italic" }}>Chưa phân công</span>;
            }
        }
    ]

    return (
        <div className='m-sm-30'>
            <div className="mb-sm-30">
                <Grid container spacing={2} justifyContent='space-between' alignItems='center'>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                            <h2 style={{ margin: 0, marginBottom: '16px' }}>CRUD TimeSheet</h2>
                            <Button variant='contained' color='primary' onClick={handleAddItem}>Thêm mới</Button>
                        </div>
                    </Grid>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <div style={{ width: '100%', maxWidth: '350px' }}>
                                <GlobitsSearchInput search={handleSearch} />
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>

            <Grid container spacing={3}>
                <Grid item xs={12} md={3}>
                    <Paper elevation={3}>
                        <div style={{ padding: "16px", backgroundColor: "#00acc1", color: "#fff", textAlign: "center" }}>
                            <Typography variant="h6">Danh sách dự án:</Typography>
                        </div>
                        <List component="nav">
                            <ListItem
                                button
                                selected={selectedProjectId === null}
                                onClick={() => setSelectedProjectId(null)}
                            >
                                <ListItemText primary="Tất cả" style={{ textAlign: "center", color: "#f57c00" }} />
                            </ListItem>

                            {projectList.map((project) => (
                                <ListItem
                                    button
                                    key={project.id}
                                    selected={selectedProjectId === project.id}
                                    onClick={() => setSelectedProjectId(project.id)}
                                >
                                    <ListItemText primary={project.name || project.code} style={{ textAlign: "center", color: "#f57c00" }} />
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                </Grid>
                
                <Grid item xs={12} md={9}>
                    <GlobitsTable
                        data={itemList ? itemList.slice() : []}
                        columns={columns}
                        totalPages={totalPages}
                        handleChangePage={handleChangePage}
                        setRowsPerPage={setRowsPerPage}
                        pageSize={pageSize}
                        pageSizeOption={[5, 10, 25]}
                        totalElements={totalElements}
                        page={page}
                        selection={false}
                        handleSelectList={() => { }}
                    />
                </Grid>
            </Grid>

            {shouldOpenDialog && (
                <TimeSheetDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}
            
            {shouldOpenConfirmationDialog && (
                <GlobitsConfirmationDialog
                    title="Xác nhận xóa"
                    text="Bạn có chắc chắn muốn xóa timesheet này không?"
                    open={shouldOpenConfirmationDialog}
                    onConfirmDialogClose={handleCloseConfirmationDialog}
                    onYesClick={handleConfirmDelete}
                    agree="Xác nhận"
                    cancel="Hủy"
                />
            )}
        </div >
    )
})
