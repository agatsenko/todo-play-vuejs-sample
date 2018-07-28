import { Component, Prop, Vue } from "vue-property-decorator";
import { TodoItem } from "@/model/todo";

@Component
export default class SimpleTodoItemComponent extends Vue {
  @Prop({ required: true }) rowJustify!: string;
  @Prop({ required: true }) rowGutter!: number;
  @Prop({ required: true }) smOuterColSpan!: number;
  @Prop({ required: true }) smMiddleColSpan!: number;
  @Prop({ required: true }) mdOuterColSpan!: number;
  @Prop({ required: true }) mdMiddleColSpan!: number;
  @Prop({ required: true }) lgOuterColSpan!: number;
  @Prop({ required: true }) lgMiddleColSpan!: number;

  @Prop({ required: true }) todoItem!: TodoItem;

  get completed(): boolean {
    return this.todoItem.completed;
  }

  set completed(completed: boolean) {
    this.todoItem.completed = completed;
  }

  remove(): void {
    this.$emit("remove", this.todoItem.id);
  }
}
