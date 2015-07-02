<g:if test="${bean }">
<g:each in="${bean }" var="b">
${b.host } ${b.username }<br/>
</g:each>
</g:if>
<g:else>
no bean
</g:else>