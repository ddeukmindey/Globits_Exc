import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { withTranslation } from "react-i18next";

const TimeSheetIndex = EgretLoadable({
  loader: () => import("./TimeSheetIndex"),
});

const ViewComponent = withTranslation()(TimeSheetIndex);

const timeSheetRoutes = [
  {
    path: ConstantList.ROOT_PATH + "project_manager/timesheet",
    exact: true,
    component: ViewComponent,
  },
];

export default timeSheetRoutes;
