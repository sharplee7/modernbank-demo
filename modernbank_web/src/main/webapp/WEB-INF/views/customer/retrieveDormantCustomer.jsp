<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<br />
	<h2>
		<span>휴면고객조회</span>
	</h2>
	※ 휴면고객은 작업이력이 1분 동안 없는 고객입니다.
	<br />
	<br />
	휴면고객목록
	<table class="search_results" cellpadding="0" cellspacing="0" id="transferHistoryList">
			<tbody id="dormantCustomerList">
				<tr>
					<th class="width135">고객ID</th>
					<th class="width135">고객명</th>
					<th class="width135">최근작업일시</th>
					<th class="width135">최근작업구분</th>
					<th class="width135">작업계좌번호</th>
					<th class="width135">작업거래금액</th>
					<th class="width135">작업상태코드</th>
				</tr>
			</tbody>
		</table>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/customer/retrieveDormantCustomer.js"></script>

