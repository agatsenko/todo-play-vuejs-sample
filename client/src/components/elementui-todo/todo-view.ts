import EuiTodoList from "@/components/elementui-todo/todo-list";
import { todoApi, createTodoList } from "@/components/elementui-todo/todo-model";
import { ITodoList } from "@/model/todo2";
import { ElInput } from "element-ui/types/input";
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: {
    EuiTodoList,
  },
})
export default class EuiTodoView extends Vue {
  newListName: string | null = null;

  lists: ITodoList[] = [];

  get canAddList(): boolean {
    return this.newListName !== null && this.newListName !== "";
  }

  mounted(): void {
    this.$nextTick(() => {
      const input = this.$refs.newListInput as ElInput;
      input.focus();
      this.refreshLists();
    });
  }

  addList(): void {
    if (this.canAddList) {
      todoApi.addList(this.newListName!).
        then(list => {
          this.lists.push(list);
        }).
        catch(err => alert(err.message)).
        then(() => this.newListName = "");
    }
  }

  deleteList(list: ITodoList): void {
    const foundIndex = this.lists.findIndex(iterList => iterList === list);
    if (foundIndex > -1)  {
      todoApi.deleteList(this.lists[foundIndex].id!).
        then(() => {
          this.lists.splice(foundIndex, 1);
        }).
        catch(err => alert(err.message));
    }
  }

  private refreshLists(): void {
    todoApi.getLists().
      then(lists => this.lists = lists).
      catch(err => alert(err.message));
  }
}
