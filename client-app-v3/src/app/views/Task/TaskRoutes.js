import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";

const TaskIndex = EgretLoadable({
  loader: () => import("./TaskIndex"),
});

const Routes = [
  {
    path: ConstantList.ROOT_PATH + "project_manager/task",
    exact: true,
    component: TaskIndex,
  },
];

export default Routes;
