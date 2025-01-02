<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<br />
	<h2>
		<span>고객 등록</span>
	</h2>

	<form id="form1" onsubmit="return false;">
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width135">고객ID</th>
					<td><input class="txt" id="cstmId" name="cstmId" type="text" /></td>
	
					<th class="width135">고객명</th>
					<td><input class="txt" id="cstmNm" name="cstmNm" type="text" /></td>
				</tr>
				<tr>
					<th class="width135">나이</th>
					<td><input class="txt" id="cstmAge" name="cstmAge" type="text" /></td>
					<th class="width135">성별</th>
					<td>
						<input type="radio" name="cstmGnd" id="cstmGnd1" value="1" checked="checked" /> 
							<label for="cstmGnd1">남</label> 
						<input type="radio"	name="cstmGnd" id="cstmGnd2" value="2" /> 
							<label for="cstmGnd2">여</label>
					</td>
				</tr>
				<tr>
					<th class="width135">주소</th>
					<td><input id="cstmAdr" name="cstmAdr" type='text'/></td>
					<th class="width135">전화번호</th>
					<td><input id="cstmPn" name="cstmPn" type='text'></td>
				</tr>
			</tbody>
		</table>
	</form>
	<br />
	<div><span>※  신규 고객은 1일 이체한도 5억, 1회 이체한도 5000만원으로 설정됩니다.</span></div>
	<div class="g_tablebtn">
		<input id="btnCreate" type="button" value="등록" class="g_btn_bg" />
	</div>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/customer/createCustomer.js" charset="UTF-8"></script>
