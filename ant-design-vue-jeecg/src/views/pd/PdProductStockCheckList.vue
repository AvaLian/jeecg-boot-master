<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="盘点科室">
              <a-input placeholder="请选择科室" v-model="queryParam.deptName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="盘点完成状态">
              <a-select v-model="queryParam.checkStatus" placeholder="请选择盘点完成状态">
                <a-select-option value="0">临时保存</a-select-option>
                <a-select-option value="1">盘点完成</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
         <!-- <template :md="6" v-if="toggleSearchStatus">
              &lt;!&ndash;待扩展查询条件&ndash;&gt;
          </template>-->
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">开始盘点</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('盘点记录表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
         <span slot="action" slot-scope="text, record">
          <a  @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <pdProductStockCheck-modal ref="modalForm" @ok="modalFormOk"></pdProductStockCheck-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin ,handleEdit,batchDel} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdProductStockCheckModal from './modules/PdProductStockCheckModal'

  export default {
    name: "PdProductStockCheckList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockCheckModal
    },
    data () {
      return {
        description: '盘点记录表管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'盘点编号',
            align:"center",
            dataIndex: 'checkNo'
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'盘点日期',
            align:"center",
            dataIndex: 'checkDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'盘点人',
            align:"center",
            dataIndex: 'checkName'
          },
          {
            title:'盘点总数量',
            align:"center",
            dataIndex: 'shouldCount'
          },
          {
            title:'已盘总数量',
            align:"center",
            dataIndex: 'checkCount'
          },
          {
            title:'盘盈盘亏数量',
            align:"center",
            dataIndex: 'profitLossCount'
          },
          {
            title:'盘点状态',
            align:"center",
            dataIndex: 'checkStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['checkStatus'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdProductStockCheck/list",
          delete: "/pd/pdProductStockCheck/delete",
          deleteBatch: "/pd/pdProductStockCheck/deleteBatch",
          exportXlsUrl: "/pd/pdProductStockCheck/exportXls"
        },
        dictOptions:{
          checkStatus:[],

        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      batchDel: function () { //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          var checkNos="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let checkStatus= this.selectionRows[a].checkStatus;
            if(checkStatus=='1'){
              checkNos+=this.selectionRows[a].checkNo + ",";
            }else{
              ids += this.selectionRows[a].id + ",";
            }
          }
          if(checkNos != ""){
            this.$message.warning("盘点编号["+checkNos.substring(0,checkNos.length-1)+"]已盘点完成，不允许删除！")
            return
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      handleEdit: function (record) { //修改
        if(record.checkStatus=='1'){
          this.$message.warning("已完成盘点，不允许修改！")
          return
        }
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDelete: function (record) { //删除
        if(record.checkStatus=='1'){
          this.$message.warning("已盘点完成，不允许删除！")
          return
        }
        var that = this;
        var id=record.id;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      initDictConfig(){
        initDictOptions('check_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'checkStatus', res.result)
          }
        })

      }
       
    }
  }
</script>
<style scoped>

</style>