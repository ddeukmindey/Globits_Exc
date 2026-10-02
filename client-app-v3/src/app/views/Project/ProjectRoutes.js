import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { withTranslation } from "react-i18next";

const ProjectIndex = EgretLoadable({
  loader: () => import("./ProjectIndex"),
});

const ViewComponent = withTranslation()(ProjectIndex);

const projectRoutes = [
  {
    path: ConstantList.ROOT_PATH + "project_manager/project",
    exact: true,
    component: ViewComponent,
  },
];

export default projectRoutes;
