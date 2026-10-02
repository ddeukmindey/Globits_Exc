import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";

const CompanyIndex = EgretLoadable({
  loader: () => import("./CompanyIndex"),
});

const Routes = [
  {
    path: ConstantList.ROOT_PATH + "category/company",
    exact: true,
    component: CompanyIndex,
  },
];

export default Routes;
